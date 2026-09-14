package assembly.general.api.exception;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidReservationStatusErrorResponse {
    private String error;
    private String message;
    private ReservationStatus currentStatus;

    public InvalidReservationStatusErrorResponse(String error, String message, ReservationStatus currentStatus){
        this.error = error;
        this.message = message;
        this.currentStatus = currentStatus;
    }
}
