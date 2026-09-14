package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OnTimeReturnResponse implements ReturnResponse{
    private UUID reservationId;
    private LocalDateTime returnedAt;
    private Integer lateDays;
    private BigDecimal lateFee;
    private String message;

    public OnTimeReturnResponse(UUID reservationId, LocalDateTime returnedAt, Integer lateDays, BigDecimal lateFee){
        this.reservationId = reservationId;
        this.returnedAt = returnedAt;
        this.lateDays = lateDays;
        this.lateFee = lateFee;
        this.message = "Book returned successfully";
    }
}
