package assembly.general.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private String message;
    private Instant timestamp;

    public ErrorResponse() {}

    public ErrorResponse(String error, String message, Instant timestamp){
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }
}
