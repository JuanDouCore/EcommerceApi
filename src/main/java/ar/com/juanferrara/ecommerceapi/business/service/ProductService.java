package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductFilter;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductDTO createProduct(CreateProductDTO createProductDTO);
    ProductDTO uploadProductImage(Long id, MultipartFile file);
    ProductDTO getProductById(Long id);
    PageResponse listProducts(Pageable pageable);
    PageResponse listProductsByFilter(ProductFilter productFilter, int page, int size);
    ProductDTO updateProduct(Long id, CreateProductDTO createProductDTO);
    ProductDTO addStockForProduct(Long productId, int amount);
    ProductDTO deleteProduct(Long id);

    void removeStockOfProduct(Long productId, int amount);

}
