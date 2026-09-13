package assembly.general.api.service;

import assembly.general.api.dto.*;
import assembly.general.api.entity.Book;
import assembly.general.api.entity.Reservation;
import assembly.general.api.entity.ReservationStatus;
import assembly.general.api.entity.User;
import assembly.general.api.exception.BookNotFoundException;
import assembly.general.api.exception.BookUnavailableException;
import assembly.general.api.exception.ReservationLimitExceededException;
import assembly.general.api.repository.BookRepository;
import assembly.general.api.repository.ReservationRepository;
import assembly.general.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
