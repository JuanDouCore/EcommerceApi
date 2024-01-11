package ar.com.juanferrara.ecommerceapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PageResponse{

    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private List<?> content;

}
