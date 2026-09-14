package assembly.general.api.entity;

import assembly.general.api.dto.ReturnCondition;
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

    //TO-DO make it so this can't be null when the status chanegs to checked out also have to enforec the 5 limit of active rule
    @Column(name="checked_out_at")
    private LocalDateTime checkedOutAt;

    //TO-DO make it so this can't be null when the status chanegs to checked out
    @Column(name="due_date")
    private LocalDateTime dueDate;

    //TO-DO make it so this can't be null when the status chanegs to returned
    @Column(name="returned_at")
    private LocalDateTime returnedAt;

    @Column(name="renewal_count")
    private Integer renewalCount;

    @Column(name="late_days")
    private Integer lateDays;

    @Column(name="late_fee_amount")
    private BigDecimal lateFeeAmount;

    @Column(name="book_condition_at_return")
    private ReturnCondition bookConditionAtReturn;

    @Column(name="notes")
    private String notes;

    @Column(name="created_at")
    private Instant createdAt;

    public Reservation(){
        this.createdAt = Instant.now();
    };

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
            ReturnCondition bookConditionAtReturn,
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
