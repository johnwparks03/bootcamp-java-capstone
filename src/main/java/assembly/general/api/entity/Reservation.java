package assembly.general.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="reservations")
@Getter
@Setter
public class Reservation extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reservation_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ReservationStatus status;

    @Column(name="reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name="checked_out_at")
    private LocalDateTime checkedOutAt;

    @Column(name="due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name="returned_at")
    private LocalDateTime returnedAt;

    @Column(name="renewal_count")
    private Integer renewalCount;

    @Column(name="late_days")
    private Integer lateDays;

    @Column(name="late_fee_amount")
    private BigDecimal lateFeeAmount;

    @Column(name="book_condition_at_return")
    private String bookConditionAtReturn;

    @Column(name="notes")
    private String notes;

    @Column(name="created_at")
    private Instant createdAt;

    protected Reservation(){};

    public Reservation(
            Book book,
            User user,
            ReservationStatus status,
            LocalDateTime reservedAt,
            LocalDateTime expiresAt,
            LocalDateTime checkedOutAt,
            LocalDateTime dueDate,
            LocalDateTime returnedAt,
            Integer renewalCount,
            Integer lateDays,
            BigDecimal lateFeeAmount,
            String bookConditionAtReturn,
            String notes
    ){
        this.book = book;
        this.user = user;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiresAt = expiresAt;
        this.checkedOutAt = checkedOutAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
        this.renewalCount = renewalCount;
        this.lateDays = lateDays;
        this.lateFeeAmount = lateFeeAmount;
        this.bookConditionAtReturn = bookConditionAtReturn;
        this.notes = notes;
        this.createdAt = Instant.now();
    }
}
