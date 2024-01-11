package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductCategoryDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductCategoryDtoMapperImpl;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductCategoryDTOMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductCategoryDTOMapperImpl;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.ProductCategory;
import ar.com.juanferrara.ecommerceapi.persistence.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceImplTest {

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Spy
    private ProductCategoryDTOMapper productCategoryDTOMapper = new ProductCategoryDTOMapperImpl();

    @Spy
    private CreateProductCategoryDtoMapper createProductCategoryDtoMapper = new CreateProductCategoryDtoMapperImpl();

    private ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        productCategory = ProductCategory.builder()
                .id(1L)
                .name("category")
                .description("description")
                .build();
    }

    @Test
    void createCategory() {
        CreateProductCategoryDTO createProductCategoryDTO = createProductCategoryDtoMapper.toDto(productCategory);
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(productCategory);

        ProductCategoryDTO productCategoryDTOSaved = productCategoryService.createCategory(createProductCategoryDTO);

        verify(productCategoryRepository).save(any(ProductCategory.class));
        assertAll(() -> {
            assertEquals(1L, productCategoryDTOSaved.getId());
            assertEquals("category", productCategoryDTOSaved.getName());
            assertEquals("description", productCategoryDTOSaved.getDescription());
        });
    }

    @Test
    void getCategoryById() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.ofNullable(productCategory));

        ProductCategoryDTO productCategoryDTO = productCategoryService.getCategoryById(1L);

        verify(productCategoryRepository).findById(1L);
        assertAll(() -> {
            assertEquals(1L, productCategoryDTO.getId());
            assertEquals("category", productCategoryDTO.getName());
            assertEquals("description", productCategoryDTO.getDescription());
        });
    }

    @Test
    void getCategoryByName() {
        when(productCategoryRepository.findByName("category")).thenReturn(Optional.ofNullable(productCategory));

        ProductCategoryDTO productCategoryDTO = productCategoryService.getCategoryByName("category");

        verify(productCategoryRepository).findByName("category");
        assertAll(() -> {
            assertEquals(1L, productCategoryDTO.getId());
            assertEquals("category", productCategoryDTO.getName());
            assertEquals("description", productCategoryDTO.getDescription());
        });
    }

    @Test
    void updateCategory() {
        CreateProductCategoryDTO createProductCategoryDTO = CreateProductCategoryDTO.builder()
                .name("category 2")
                .description("description")
                .build();
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.ofNullable(productCategory));

        ProductCategoryDTO productCategoryDTOModified = productCategoryService.updateCategory(1L, createProductCategoryDTO);

        verify(productCategoryRepository).findById(1L);
        assertAll(() -> {
            assertEquals(1L, productCategoryDTOModified.getId());
            assertEquals("category 2", productCategoryDTOModified.getName());
            assertEquals("description", productCategoryDTOModified.getDescription());
        });
    }

    @Test
    void deleteCategory() {
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.ofNullable(productCategory));

        ProductCategoryDTO productCategoryDTO = productCategoryService.deleteCategory(1L);

        verify(productCategoryRepository).findById(1L);
        assertAll(() -> {
            assertEquals(1L, productCategoryDTO.getId());
            assertEquals("category", productCategoryDTO.getName());
            assertEquals("description", productCategoryDTO.getDescription());
        });
    }

    @Test
    void getAllCategories() {
        when(productCategoryRepository.findAll()).thenReturn(List.of(productCategory));

        java.util.List<ProductCategoryDTO> productCategoryDTOList = productCategoryService.getAllCategories();

        verify(productCategoryRepository).findAll();
        assertAll(() -> {
            assertEquals(1, productCategoryDTOList.size());
            assertEquals(1L, productCategoryDTOList.get(0).getId());
            assertEquals("category", productCategoryDTOList.get(0).getName());
            assertEquals("description", productCategoryDTOList.get(0).getDescription());
        });
    }
}