package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductDtoMapperImpl;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductDtoMapperImpl;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import ar.com.juanferrara.ecommerceapi.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Spy
    private CreateProductDtoMapper createProductDtoMapper = new CreateProductDtoMapperImpl();
    @Spy
    private ProductDtoMapper productDtoMapper = new ProductDtoMapperImpl();


    Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("product")
                .description("description")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();
    }

    /*@Test
    void createProduct() {
        CreateProductDTO createProductDTO = CreateProductDTO.builder()
                .name("product")
                .description("description")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO productDTO = productService.createProduct(createProductDTO);

        verify(productRepository).save(any(Product.class));
        assertAll(() -> {
            assertEquals(1L, productDTO.getId());
            assertEquals("product", productDTO.getName());
            assertEquals("description", productDTO.getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTO.getPrice());
            assertEquals(10, productDTO.getStock());
        });
    }*/

    @Test
    void getProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        ProductDTO productDTO = productService.getProductById(1L);

        verify(productRepository).findById(1L);
        assertAll(() -> {
            assertEquals(1L, productDTO.getId());
            assertEquals("product", productDTO.getName());
            assertEquals("description", productDTO.getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTO.getPrice());
            assertEquals(10, productDTO.getStock());
        });
    }

    /*@Test

    void listProducts() {
        Product product2 = Product.builder()
                .id(2L)
                .name("product2")
                .description("description2")
                .price(BigDecimal.valueOf(200.0))
                .stock(20)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = new PageImpl<>(List.of(product, product2));
        when(productRepository.findAll(pageable)).thenReturn(products);

        //Page<ProductDTO> productDTOPage = productService.listProducts(pageable);

        verify(productRepository).findAll(pageable);
        assertAll(() -> {
            assertEquals(2, productDTOPage.getTotalElements());
            assertEquals(1L, productDTOPage.getContent().get(0).getId());
            assertEquals("product", productDTOPage.getContent().get(0).getName());
            assertEquals("description", productDTOPage.getContent().get(0).getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTOPage.getContent().get(0).getPrice());
            assertEquals(10, productDTOPage.getContent().get(0).getStock());
            assertEquals(2L, productDTOPage.getContent().get(1).getId());
            assertEquals("product2", productDTOPage.getContent().get(1).getName());
            assertEquals("description2", productDTOPage.getContent().get(1).getDescription());
            assertEquals(BigDecimal.valueOf(200.0), productDTOPage.getContent().get(1).getPrice());
            assertEquals(20, productDTOPage.getContent().get(1).getStock());
        });
    }

    @Test
    /*void listProductsByFilter() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Example Product");
        Pageable pageable = PageRequest.of(0, 10);

        Example<Product> exampleProduct = Example.of(productDtoMapper.toEntity(productDTO));
        when(productRepository.findAll(exampleProduct, pageable)).thenReturn(createMockProductPage());

        Page<ProductDTO> productDTOPage = productService.listProductsByFilter(productDTO, pageable);

        verify(productRepository).findAll(exampleProduct, pageable);
        assertAll(() -> {
            assertEquals(1, productDTOPage.getTotalElements());
            assertEquals(1L, productDTOPage.getContent().get(0).getId());
            assertEquals("Example Product", productDTOPage.getContent().get(0).getName());
            assertEquals("description", productDTOPage.getContent().get(0).getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTOPage.getContent().get(0).getPrice());
            assertEquals(10, productDTOPage.getContent().get(0).getStock());
        });
    }*/

    private Page<Product> createMockProductPage() {
        Product product1 = product;
        product1.setName("Example Product");
        return new PageImpl<>(List.of(product1));
    }

    @Test
    void updateProduct() {
        CreateProductDTO createProductDTO = CreateProductDTO.builder()
                .name("product updated")
                .description("description")
                .price(BigDecimal.valueOf(100.0))
                .stock(10)
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO productDTO = productService.updateProduct(1L, createProductDTO);

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
        assertAll(() -> {
            assertEquals(1L, productDTO.getId());
            assertEquals("product updated", productDTO.getName());
            assertEquals("description", productDTO.getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTO.getPrice());
            assertEquals(10, productDTO.getStock());
        });
    }

    @Test
    void deleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        ProductDTO productDTO = productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
        assertAll(() -> {
            assertEquals(1L, productDTO.getId());
            assertEquals("product", productDTO.getName());
            assertEquals("description", productDTO.getDescription());
            assertEquals(BigDecimal.valueOf(100.0), productDTO.getPrice());
            assertEquals(10, productDTO.getStock());
        });
    }
}