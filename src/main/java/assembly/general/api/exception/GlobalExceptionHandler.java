package assembly.general.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException exception){
        ErrorResponse response = new ErrorResponse(
                "NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException exception){
        ErrorResponse response = new ErrorResponse(
                "NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReservationLimitExceededException.class)
    public ResponseEntity<ReservationLimitExceededErrorResponse> handleReservationLimitExceeded(ReservationLimitExceededException exception){
        ReservationLimitExceededErrorResponse response = new ReservationLimitExceededErrorResponse(
                "RESERVATION_LIMIT_EXCEEDED",
                exception.getMessage(),
                5
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<BookUnavailableErrorResponse> handleReservationLimitExceeded(BookUnavailableException exception){
        BookUnavailableErrorResponse response = new BookUnavailableErrorResponse(
                "BOOK_UNAVAILABLE",
                exception.getMessage(),
                0
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ForbiddenNotLibrarianException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenNotLibrarian(ForbiddenNotLibrarianException exception){
        ErrorResponse response = new ErrorResponse(
                "FORBIDDEN",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(InvalidReservationStatusException.class)
    public ResponseEntity<InvalidReservationStatusErrorResponse> handleInvalidStatusAtCheckout(InvalidReservationStatusException exception){
        InvalidReservationStatusErrorResponse response = new InvalidReservationStatusErrorResponse(
                "INVALID_STATUS",
                exception.getMessage(),
                exception.getCurrentStatus()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFound(ProfileNotFoundException exception){
        ErrorResponse response = new ErrorResponse(
                "NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException exception){
        ErrorResponse response = new ErrorResponse(
                "AUTHENTICATION_FAILED",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                message,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolations(ConstraintViolationException exception){
        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception){
        ErrorResponse response = new ErrorResponse(
                "BAD_REQUEST",
                exception.getMessage(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception) {

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                Instant.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
