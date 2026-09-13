package assembly.general.api.service;

import assembly.general.api.dto.CreateReservationResponse;
import assembly.general.api.entity.Book;
import assembly.general.api.entity.Reservation;
import assembly.general.api.entity.ReservationStatus;
import assembly.general.api.entity.User;
import assembly.general.api.exception.BookNotFoundException;
import assembly.general.api.exception.BookUnavailableException;
import assembly.general.api.exception.ProfileNotFoundException;
import assembly.general.api.exception.ReservationLimitExceededException;
import assembly.general.api.repository.BookRepository;
import assembly.general.api.repository.ReservationRepository;
import assembly.general.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
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

    public CreateReservationResponse createReservation(UUID userId, UUID bookId){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new RuntimeException();
        }
        User user = userOpt.get();

        if(reservationRepository.getUserActiveReservations(userId) >= 5){
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
}
