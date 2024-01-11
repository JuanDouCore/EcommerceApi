package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.PageResponseMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.CreateProductDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.product.ProductFilterMapper;
import ar.com.juanferrara.ecommerceapi.business.service.ProductService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductFilter;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderBy;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.ProductCategoryRepository;
import ar.com.juanferrara.ecommerceapi.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductDtoMapper productDtoMapper;
    @Autowired
    private CreateProductDtoMapper createProductDtoMapper;
    @Autowired
    private ProductFilterMapper productFilterMapper;

    @Override
    public ProductDTO createProduct(CreateProductDTO createProductDTO) {
        Product product = createProductDtoMapper.toEntity(createProductDTO);
        product.setCategory(productCategoryRepository.findById(createProductDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found")));

        return productDtoMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDTO uploadProductImage(Long id, MultipartFile file) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if(product.getImageUrl() != null)
            imageService.deleteImage(product.getImageUrl());

        product.setImageUrl(imageService.uploadImage(file, product.getName()));

        return productDtoMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return productDtoMapper.toDto(product);
    }

    @Override
    public PageResponse listProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        products.getPageable().getPageNumber();
        return PageResponseMapper.convertToPageResponse(products);
    }

    @Override
    public PageResponse listProductsByFilter(ProductFilter productFilter, int page, int size) {
        Product productToFindExample = productFilterMapper.toEntity(productFilter);
        if(productFilter.getCategoryName() != null)
            productToFindExample.setCategory(productCategoryRepository.findByName(productFilter.getCategoryName())
                    .orElseThrow(() -> new NotFoundException("Category not found")));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Product> exampleProduct = Example.of(productToFindExample, exampleMatcher);

        Pageable pageable;
        if(productFilter.getOrderBy() != null)
            pageable = (productFilter.getOrderBy().equals(OrderBy.ASC))
                    ? PageRequest.of(page, size, Sort.by("price").ascending())
                    : PageRequest.of(page, size, Sort.by("price").descending());
        else
            pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAll(exampleProduct, pageable);


        //return products.map(productDtoMapper::toDto);
        return PageResponseMapper.convertToPageResponse(products);
    }

    @Override
    public ProductDTO updateProduct(Long id, CreateProductDTO createProductDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        createProductDtoMapper.updateEntity(product, createProductDtoMapper.toEntity(createProductDTO));

        product.setId(id);
        productRepository.save(product);

        return productDtoMapper.toDto(product);
    }

    @Override
    public ProductDTO addStockForProduct(Long productId, int amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setStock(product.getStock() + amount);

        return productDtoMapper.toDto(productRepository.save(product));
    }

    @Override
    public void removeStockOfProduct(Long productId, int amount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setStock(product.getStock() - amount);

        productRepository.save(product);
    }

    @Override
    public ProductDTO deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productRepository.deleteById(id);

        if(product.getImageUrl() != null)
            imageService.deleteImage(product.getImageUrl());

        return productDtoMapper.toDto(product);
    }


}
