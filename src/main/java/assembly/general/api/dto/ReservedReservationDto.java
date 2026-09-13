package assembly.general.api.dto;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
public class ReservedReservationDto implements ReservationDto {
    private UUID reservationId;
    private UUID bookId;
    private String bookTitle;
    private String bookAuthor;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    private Integer daysUntilExpiry;

    public ReservedReservationDto(
            UUID reservationId,
            UUID bookId,
            String bookTitle,
            String bookAuthor,
            ReservationStatus status,
            LocalDateTime reservedAt,
            LocalDateTime expiresAt
    ){
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiresAt = expiresAt;
        this.daysUntilExpiry = Math.toIntExact(ChronoUnit.DAYS.between(reservedAt, expiresAt));
    }
}
