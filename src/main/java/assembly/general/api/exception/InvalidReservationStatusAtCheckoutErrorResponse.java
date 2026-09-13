package assembly.general.api.exception;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InvalidReservationStatusAtCheckoutErrorResponse {
    private String error;
    private String message;
    private ReservationStatus currentStatus;

    public InvalidReservationStatusAtCheckoutErrorResponse(String error, String message, ReservationStatus currentStatus){
        this.error = error;
        this.message = message;
        this.currentStatus = currentStatus;
    }
}
