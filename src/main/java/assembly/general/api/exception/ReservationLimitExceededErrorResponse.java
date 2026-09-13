package assembly.general.api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationLimitExceededErrorResponse {
    private String error;
    private String message;
    private Integer currentReservations;

    public ReservationLimitExceededErrorResponse(
            String error, String message, Integer currentReservations
    ){
        this.error = error;
        this.message = error;
        this.currentReservations = currentReservations;
    }
}
