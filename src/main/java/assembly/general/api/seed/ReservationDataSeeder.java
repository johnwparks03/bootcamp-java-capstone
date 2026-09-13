package assembly.general.api.seed;

import assembly.general.api.entity.Book;
import assembly.general.api.entity.Reservation;
import assembly.general.api.entity.ReservationStatus;
import assembly.general.api.entity.User;
import assembly.general.api.repository.BookRepository;
import assembly.general.api.repository.ReservationRepository;
import assembly.general.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Order(3)
public class ReservationDataSeeder implements CommandLineRunner {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationDataSeeder(
            ReservationRepository reservationRepository,
            BookRepository bookRepository,
            UserRepository userRepository
    ){
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        reservationRepository.deleteAll();

        User john = userRepository.findByEmail("john.doe@test.com").orElseThrow();


        Book cleanCode = bookRepository.findByIsbn("978-0-13-468599-1").orElseThrow(() -> new IllegalStateException("ISBN 978-0-13-468599-1 does not exist"));
        Book designPatterns = bookRepository.findByIsbn("978-0-201-63361-0").orElseThrow(() -> new IllegalStateException("ISBN 978-0-201-63361-0 does not exist"));
        Book greatGatsby = bookRepository.findByIsbn("978-0-7432-7356-5").orElseThrow(() -> new IllegalStateException("ISBN 978-0-7432-7356-5 does not exist"));
        Book headFirstJava = bookRepository.findByIsbn("978-0-596-52068-7").orElseThrow(() -> new IllegalStateException("ISBN 978-0-596-52068-7 does not exist"));

        Reservation reservation1 = new Reservation();
        reservation1.setBook(cleanCode);
        reservation1.setUser(john);
        reservation1.setStatus(ReservationStatus.RESERVED);
        reservation1.setReservedAt(LocalDateTime.of(2026, 9, 10, 10, 0));
        reservation1.setExpiresAt(LocalDateTime.of(2026, 9, 17, 10, 0));

        Reservation reservation2 = new Reservation();
        reservation2.setBook(designPatterns);
        reservation2.setUser(john);
        reservation2.setStatus(ReservationStatus.CHECKED_OUT);
        reservation2.setReservedAt(LocalDateTime.of(2026, 9, 1, 10, 0));
        reservation2.setExpiresAt(LocalDateTime.of(2026, 9, 8, 10, 0));
        reservation2.setCheckedOutAt(LocalDateTime.of(2026, 9, 2, 14, 0));
        reservation2.setDueDate(LocalDateTime.of(2026, 9, 16, 14, 0));

        Reservation reservation3 = new Reservation();
        reservation3.setBook(greatGatsby);
        reservation3.setUser(john);
        reservation3.setStatus(ReservationStatus.RETURNED);
        reservation3.setReservedAt(LocalDateTime.of(2026, 8, 1, 10, 0));
        reservation3.setExpiresAt(LocalDateTime.of(2026, 8, 8, 10, 0));
        reservation3.setCheckedOutAt(LocalDateTime.of(2026, 8, 2, 14, 0));
        reservation3.setDueDate(LocalDateTime.of(2026, 8, 16, 14, 0));
        reservation3.setReturnedAt(LocalDateTime.of(2026, 8, 15, 9, 0));

        Reservation reservation4 = new Reservation();
        reservation4.setBook(headFirstJava);
        reservation4.setUser(john);
        reservation4.setStatus(ReservationStatus.RETURNED);
        reservation4.setReservedAt(LocalDateTime.of(2026, 7, 20, 10, 0));
        reservation4.setExpiresAt(LocalDateTime.of(2026, 7, 27, 10, 0));
        reservation4.setCheckedOutAt(LocalDateTime.of(2026, 7, 21, 14, 0));
        reservation4.setDueDate(LocalDateTime.of(2026, 8, 4, 14, 0));
        reservation4.setReturnedAt(LocalDateTime.of(2026, 8, 6, 9, 0));

        Reservation reservation5 = new Reservation();
        reservation5.setBook(designPatterns);
        reservation5.setUser(john);
        reservation5.setStatus(ReservationStatus.RETURNED);
        reservation5.setReservedAt(LocalDateTime.of(2026, 6, 15, 10, 0));
        reservation5.setExpiresAt(LocalDateTime.of(2026, 6, 22, 10, 0));
        reservation5.setCheckedOutAt(LocalDateTime.of(2026, 6, 16, 14, 0));
        reservation5.setDueDate(LocalDateTime.of(2026, 6, 30, 14, 0));
        reservation5.setReturnedAt(LocalDateTime.of(2026, 6, 28, 9, 0));

        reservationRepository.saveAll(List.of(
                reservation1,
                reservation2,
                reservation3,
                reservation4,
                reservation5
        ));
    }
}
