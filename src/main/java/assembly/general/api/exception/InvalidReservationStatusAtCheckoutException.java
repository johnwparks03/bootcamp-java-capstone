package assembly.general.api.exception;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidReservationStatusAtCheckoutException extends RuntimeException{
    private ReservationStatus currentStatus;

    public InvalidReservationStatusAtCheckoutException (ReservationStatus currentStatus) {
        this.currentStatus = currentStatus;
        super("Can only checkout reservations with RESERVED status");
    }
}
