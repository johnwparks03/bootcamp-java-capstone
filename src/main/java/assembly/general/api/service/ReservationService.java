package assembly.general.api.service;

import assembly.general.api.dto.*;
import assembly.general.api.entity.*;
import assembly.general.api.exception.*;
import assembly.general.api.repository.BookRepository;
import assembly.general.api.repository.ReservationRepository;
import assembly.general.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository){
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReservationsResponse getReservations(UUID userId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        List<Reservation> userActiveReservations = reservationRepository.getUserActiveReservations(user.getId());

        List<ReservationDto> reservationDtos = userActiveReservations.stream().map(this::reservationToReservationDtoMapper).toList();

        return new ReservationsResponse(
                reservationDtos,
                reservationDtos.size()
        );
    }

    public CreateReservationResponse createReservation(UUID userId, UUID bookId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        if(reservationRepository.getUserActiveReservationsCount(userId) >= 5){
            throw new ReservationLimitExceededException();
        }

        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if(bookOpt.isEmpty()){
            throw new BookNotFoundException(bookId);
        }
        Book book = bookOpt.get();

        if(book.getAvailableCopies() <= 0){
            throw new BookUnavailableException();
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setExpiresAt(LocalDateTime.now().plusDays(7));

        reservation = reservationRepository.save(reservation);

        book.decrementAvailableCopies();
        book = bookRepository.save(book);

        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getBook().getId(),
                reservation.getUser().getId(),
                reservation.getBook().getTitle(),
                reservation.getStatus(),
                reservation.getReservedAt(),
                reservation.getExpiresAt()
        );
    }

    private ReservationDto reservationToReservationDtoMapper(Reservation reservation){
        if(reservation.getStatus() == ReservationStatus.RESERVED){
            return new ReservedReservationDto(
                    reservation.getId(),
                    reservation.getBook().getId(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getStatus(),
                    reservation.getReservedAt(),
                    reservation.getExpiresAt()
            );
        } else {
            return new CheckedOutReservationDto(
                    reservation.getId(),
                    reservation.getBook().getId(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getStatus(),
                    reservation.getCheckedOutAt(),
                    reservation.getDueDate()
            );
        }
    }

    public CheckoutResponse checkoutReservation(UUID userId, UUID reservationId, CheckoutRequest request){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        if(user.getRole() != Role.LIBRARIAN){
            throw new ForbiddenNotLibrarianException("checkout");
        }

        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if(reservationOpt.isEmpty()){
            throw new ReservationNotFoundException(reservationId);
        }
        Reservation reservation = reservationOpt.get();

        if(reservation.getStatus() != ReservationStatus.RESERVED){
            throw new InvalidReservationStatusException(reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setCheckedOutAt(LocalDateTime.now());
        reservation.setDueDate(LocalDateTime.now().plusDays(14));
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            reservation.setNotes(request.getNotes().trim());
        }
        reservation = reservationRepository.save(reservation);

        return new CheckoutResponse(
                reservation.getId(),
                reservation.getStatus(),
                reservation.getCheckedOutAt(),
                reservation.getDueDate()
        );
    }

    public ReturnResponse returnBook(UUID userId, UUID reservationId, ReturnRequest request){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        if(user.getRole() != Role.LIBRARIAN){
            throw new ForbiddenNotLibrarianException("return");
        }

        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if(reservationOpt.isEmpty()){
            throw new ReservationNotFoundException(reservationId);
        }
        Reservation reservation = reservationOpt.get();

        if(reservation.getStatus() != ReservationStatus.CHECKED_OUT){
            throw new InvalidReservationStatusException(reservation.getStatus());
        }

        Book book = reservation.getBook();
        book.incrementAvailableCopies();
        book = bookRepository.save(book);

        LocalDateTime returnedDate = LocalDateTime.now();
        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.setReturnedAt(returnedDate);
        reservation.setBookConditionAtReturn(request.getCondition());
        if (request.getNotes() != null && !request.getNotes().isBlank()) {
            reservation.setNotes(request.getNotes().trim());
        }
        boolean late = returnedDate.isAfter(reservation.getDueDate()) ? true : false;
        if(late){
            Integer lateDays = Math.toIntExact(ChronoUnit.DAYS.between(reservation.getDueDate(), returnedDate));
            reservation.setLateDays(lateDays);
            reservation.setLateFeeAmount(new BigDecimal(lateDays));
        }else{
            reservation.setLateDays(0);
            reservation.setLateFeeAmount(new BigDecimal(0));
        }
        reservation = reservationRepository.save(reservation);

        if(late){
            return new LateReturnResponse(
                    reservation.getId(),
                    reservation.getReturnedAt(),
                    reservation.getDueDate(),
                    reservation.getLateDays(),
                    reservation.getLateFeeAmount()
            );
        } else {
            return new OnTimeReturnResponse(
                    reservation.getId(),
                    reservation.getReturnedAt(),
                    reservation.getLateDays(),
                    reservation.getLateFeeAmount()
            );
        }

    }

    public ReservationHistoryResponse getHistory(UUID userId, Integer page, Integer size){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        Pageable pageable = PageRequest.of(
                page,
                size
        );

        Page<Reservation> reservationsPage = reservationRepository.getUserHistory(userId, pageable);

        List<ReservationDetailDto> reservationDtos = reservationsPage.stream().map(this::convertReservationToReservationDetailDto).toList();

        return new ReservationHistoryResponse(
                reservationDtos,
                reservationsPage.getNumber(),
                reservationsPage.getSize(),
                reservationsPage.getTotalElements(),
                reservationsPage.getTotalPages(),
                reservationsPage.isLast()
        );
    }

    private ReservationDetailDto convertReservationToReservationDetailDto(Reservation reservation){
        return new ReservationDetailDto(
                reservation.getId(),
                reservation.getBook().getTitle(),
                reservation.getBook().getAuthor(),
                reservation.getReservedAt(),
                reservation.getCheckedOutAt(),
                reservation.getReturnedAt(),
                reservation.getDueDate(),
                reservation.getStatus()
        );
    }
}
