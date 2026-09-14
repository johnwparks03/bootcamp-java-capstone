package assembly.general.api.dto;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CheckoutResponse {
    private UUID reservationId;
    private ReservationStatus status;
    private LocalDateTime checkedOutAt;
    private LocalDateTime dueDate;
    private String message;

    public CheckoutResponse(UUID reservationId, ReservationStatus status, LocalDateTime checkedOutAt, LocalDateTime dueDate){
        this.reservationId = reservationId;
        this.status = status;
        this.checkedOutAt = checkedOutAt;
        this.dueDate = dueDate;
        this.message = "Book checked out successfully. Due date: " + dueDate.getMonth() + " " + dueDate.getDayOfMonth() + ", " + dueDate.getYear();
    }
}
