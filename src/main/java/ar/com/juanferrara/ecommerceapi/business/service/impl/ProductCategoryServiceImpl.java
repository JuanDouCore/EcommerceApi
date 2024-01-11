package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductCategoryDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductCategoryDTOMapper;
import ar.com.juanferrara.ecommerceapi.business.service.ProductCategoryService;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.ProductCategory;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.ProductCategoryRepository;
import ar.com.juanferrara.ecommerceapi.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryDTOMapper productCategoryDTOMapper;
    @Autowired
    private CreateProductCategoryDtoMapper createProductCategoryDtoMapper;

    @Override
    public ProductCategoryDTO createCategory(CreateProductCategoryDTO createProductCategoryDTO) {
        ProductCategory productCategory = createProductCategoryDtoMapper.toEntity(createProductCategoryDTO);

        return productCategoryDTOMapper.toDto(productCategoryRepository.save(productCategory));
    }

    @Override
    public ProductCategoryDTO getCategoryById(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        return productCategoryDTOMapper.toDto(productCategory);
    }

    @Override
    public ProductCategoryDTO getCategoryByName(String name) {
        ProductCategory productCategory = productCategoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        return productCategoryDTOMapper.toDto(productCategory);
    }

    @Override
    public ProductCategoryDTO updateCategory(Long id, CreateProductCategoryDTO createProductCategoryDTO) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        createProductCategoryDtoMapper.updateEntity(productCategory, createProductCategoryDtoMapper.toEntity(createProductCategoryDTO));

        productCategory.setId(id);
        productCategoryRepository.save(productCategory);

        return productCategoryDTOMapper.toDto(productCategory);
    }

    @Override
    public ProductCategoryDTO deleteCategory(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if(productRepository.existsByCategory(productCategory))
            throw new GenericException("CATEGORY CONFLICT", "Category cannot be deleted because it is in use", HttpStatus.CONFLICT);

        productCategoryRepository.deleteById(id);

        return productCategoryDTOMapper.toDto(productCategory);
    }

    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        return productCategoryDTOMapper.toDTOList(productCategoryRepository.findAll());
    }
}
