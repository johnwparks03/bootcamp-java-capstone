package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LateReturnResponse implements ReturnResponse{
    private UUID reservationId;
    private LocalDateTime returnedAt;
    private LocalDateTime dueDate;
    private Integer lateDays;
    private BigDecimal lateFee;
    private String message;

    public LateReturnResponse(UUID reservationId, LocalDateTime dueDate, LocalDateTime returnedAt, Integer lateDays, BigDecimal lateFee){
        this.reservationId = reservationId;
        this.returnedAt = returnedAt;
        this.dueDate = dueDate;
        this.lateDays = lateDays;
        this.lateFee = lateFee;
        this.message = "Book returned. Late fee of $" + lateFee + " applied to account.";
    }
}
