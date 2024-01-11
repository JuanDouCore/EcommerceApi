package ar.com.juanferrara.ecommerceapi.business.mapper;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

public class PageResponseMapper {

    public static PageResponse convertToPageResponse(Page<?> page) {
        return PageResponse.builder()
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .numberOfElements(page.getNumberOfElements())
                .content(page.getContent())
                .build();
    }
}
