package assembly.general.api.dto;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
public class CheckedOutReservationDto implements ReservationDto {
    private UUID reservationId;
    private UUID bookId;
    private String bookTitle;
    private String bookAuthor;
    private ReservationStatus status;
    private LocalDateTime checkedOutAt;
    private LocalDateTime dueDate;
    private Integer daysUntilDue;

    public CheckedOutReservationDto(
            UUID reservationId,
            UUID bookId,
            String bookTitle,
            String bookAuthor,
            ReservationStatus status,
            LocalDateTime checkedOutAt,
            LocalDateTime dueDate
    ){
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.status = status;
        this.checkedOutAt = checkedOutAt;
        this.dueDate = dueDate;
        this.daysUntilDue = Math.toIntExact(ChronoUnit.DAYS.between(checkedOutAt, dueDate));
    }
}
