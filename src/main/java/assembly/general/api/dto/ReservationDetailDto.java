package assembly.general.api.dto;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ReservationDetailDto {
    private UUID reservationId;
    private String title;
    private String author;
    private LocalDateTime reservedAt;
    private LocalDateTime checkedOutAt;
    private LocalDateTime returnedAt;
    private LocalDateTime dueDate;
    private ReservationStatus status;
    private boolean wasLate;

    public ReservationDetailDto(UUID reservationId, String title, String author, LocalDateTime reservedAt, LocalDateTime checkedOutAt, LocalDateTime returnedAt, LocalDateTime dueDate, ReservationStatus status) {
        this.reservationId = reservationId;
        this.title = title;
        this.author = author;
        this.reservedAt = reservedAt;
        this.checkedOutAt = checkedOutAt;
        this.returnedAt = returnedAt;
        this.dueDate = dueDate;
        this.status = status;
        this.wasLate = returnedAt.isAfter(dueDate);
    }
}
