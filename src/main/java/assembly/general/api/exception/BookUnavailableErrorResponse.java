package assembly.general.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BookUnavailableErrorResponse {
    private String error;
    private String message;
    private Integer availableCopies;

    public BookUnavailableErrorResponse(String error, String message, Integer availableCopies){
        this.error = error;
        this.message = message;
        this.availableCopies = availableCopies;
    }
}
