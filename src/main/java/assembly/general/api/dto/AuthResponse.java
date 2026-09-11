package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthResponse {
    private boolean success;
    private String message;
    private UserDto user;
    private String token;
    private LocalDateTime timestamp;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public AuthResponse(boolean success, String message, UserDto user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
        this.timestamp = LocalDateTime.now();
    }

    // Static factory methods for common responses
    public static AuthResponse success(String message, UserDto user, String token) {
        return new AuthResponse(true, message, user, token);
    }

    public static AuthResponse failure(String message) {
        return new AuthResponse(false, message);
    }


}
