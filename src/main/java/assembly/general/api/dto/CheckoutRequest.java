package assembly.general.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private String notes;

    public CheckoutRequest() {}

    public CheckoutRequest(String notes){
        this.notes = notes;
    }
}
