package assembly.general.api.exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException() {
        super("No copies available for reservation");
    }
}
