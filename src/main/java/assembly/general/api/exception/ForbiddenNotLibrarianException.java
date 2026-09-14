package assembly.general.api.exception;

import java.util.Objects;

public class ForbiddenNotLibrarianException extends RuntimeException {
    public ForbiddenNotLibrarianException(String process) {
        String message = "Only librarians can perform this action";
        if(Objects.equals(process, "checkout")){
            message = "Only librarians can checkout books";
        } else if (Objects.equals(process, "return")){
            message = "Only librarians can process returns";
        }
        super(message);
    }
}
