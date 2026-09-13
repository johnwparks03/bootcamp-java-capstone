package assembly.general.api.exception;

public class ForbiddenNotLibrarianException extends RuntimeException {
    public ForbiddenNotLibrarianException() {
        super("Only librarians can checkout books");
    }
}
