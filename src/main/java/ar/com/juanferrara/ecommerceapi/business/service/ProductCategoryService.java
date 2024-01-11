package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductCategoryDTO;

import java.util.List;

public interface ProductCategoryService {
    ProductCategoryDTO createCategory(CreateProductCategoryDTO createProductCategoryDTO);
    ProductCategoryDTO getCategoryById(Long id);
    ProductCategoryDTO getCategoryByName(String name);
    ProductCategoryDTO updateCategory(Long id, CreateProductCategoryDTO createProductCategoryDTO);
    ProductCategoryDTO deleteCategory(Long id);
    List<ProductCategoryDTO> getAllCategories();
}
