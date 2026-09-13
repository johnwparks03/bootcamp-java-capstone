package assembly.general.api.exception;

public class ReservationLimitExceededException extends RuntimeException {
    public ReservationLimitExceededException() {
        super("You have reached the maximum of 5 active reservations");
    }
}
