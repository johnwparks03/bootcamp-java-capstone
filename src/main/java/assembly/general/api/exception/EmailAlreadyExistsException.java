package assembly.general.api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("User registration failed. If a user exists for this email, we'll send instructions to sign in or reset your password.");
    }
}
