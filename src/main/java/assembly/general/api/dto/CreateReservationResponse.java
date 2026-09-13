package assembly.general.api.dto;

import assembly.general.api.entity.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateReservationResponse {
    private UUID reservationId;
    private UUID bookId;
    private UUID userId;
    private String bookTitle;
    private ReservationStatus status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    private String message;

    public CreateReservationResponse(
            UUID reservationId,
            UUID bookId,
            UUID userID,
            String bookTitle,
            ReservationStatus status,
            LocalDateTime reservedAt,
            LocalDateTime expiresAt
    ){
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userID;
        this.bookTitle = bookTitle;
        this.status = status;
        this.reservedAt = reservedAt;
        this.expiresAt = expiresAt;
        this.message = "Book reserved successfully. Please pick up within 7 days.";
    }
}
