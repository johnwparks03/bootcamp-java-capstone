package assembly.general.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnRequest {
    @NotNull(message = "Condition at return required: GOOD, FAIR, POOR, DAMAGED")
    private ReturnCondition condition;
    private String notes;

    public ReturnRequest(ReturnCondition condition, String notes){
        this.condition = condition;
        this.notes = notes;
    }
}
