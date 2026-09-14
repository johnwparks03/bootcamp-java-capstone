package assembly.general.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateReservationRequest {
    @NotNull(message = "bookId is required")
    private UUID bookId;

    public CreateReservationRequest(UUID bookId){
        this.bookId = bookId;
    }

}
