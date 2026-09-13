package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationsResponse {
    private List<ReservationDto> reservations;
    private Integer totalActive;

    public ReservationsResponse(List<ReservationDto> reservations, Integer totalActive){
        this.reservations = reservations;
        this.totalActive = totalActive;
    }
}
