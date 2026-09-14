package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationHistoryResponse {
    private List<ReservationDetailDto> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean last;

    public ReservationHistoryResponse(
            List<ReservationDetailDto> content,
            Integer page,
            Integer size,
            Long totalElements,
            Integer totalPages,
            boolean last
    ){
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}
