package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.ProductCategoryService;
import ar.com.juanferrara.ecommerceapi.business.service.ProductService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductFilter;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderBy;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    public ProductCategoryService productCategoryService;

    private boolean isInvalidPageRequest(int page, int size) {
        return (page < 0 || size < 0 || size > 50);
    }


    @Operation(
            tags = {"Gestor Productos"},
            operationId = "createProduct",
            summary = "Crear un producto",
            description = "Crea un producto nuevo cumpliendo con los parametros solicitados y asignadole la categoria ya existente indicada. " +
                    "Posteriormente a crear este producto se le puede asignar mediante su id una imagen de muestra\n"+
                    "\n\n"+ Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Producto a ser creado"
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO createProductDTO) {
        ProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.created(URI.create("/api/products/" + createdProduct.getId())).body(createdProduct);
    }

    @Operation(
            tags = {"Gestor Productos"},
            operationId = "productImageSet",
            summary = "Establece la imagen de un producto",
            description = "Carga la imagen principal para que el producto sea visto\n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen cargada", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PostMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> uploadProductImage(@PathVariable Long id,
                                                         @Parameter(description = "Imagen a ser cargada", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))) @RequestPart("image") MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().contains("image"))
            throw new GenericException("BAD REQUEST FILE", "The file must be an image", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(productService.uploadProductImage(id, file));
    }

    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "productGetById",
            summary = "Obtener un producto por su ID",
            description = "Obtener un producto por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "listProducts",
            summary = "Listar productos",
            description = "Lista todos los productos por pagina"
    )
    @GetMapping
    public ResponseEntity<PageResponse> listProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if(isInvalidPageRequest(page, size))
            throw new GenericException("BAD PAGEABLE", "The amount of elements cannot be more than 50, and page index must not be less than zero", HttpStatus.BAD_REQUEST);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.listProducts(pageable));
    }


    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "listProductsFilter",
            summary = "Listar productos por filtro",
            description = "Lista todos los productos filtrados por pagina"
    )
    @GetMapping("/filter")
    public ResponseEntity<PageResponse> listProductsByFilter(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(required = false) String name,
                                                                         @RequestParam(required = false) String categoryName,
                                                                         @RequestParam(required = false) OrderBy orderBy) {

        if(isInvalidPageRequest(page, size))
            throw new GenericException("BAD PAGEABLE", "The amount of elements cannot be more than 50, and page index must not be less than zero", HttpStatus.BAD_REQUEST);

        ProductFilter productFilter = new ProductFilter();

        Pageable pageable = PageRequest.of(page, size);

        if (name != null) productFilter.setName(name);
        if (categoryName != null) productFilter.setCategoryName(categoryName);
        if (orderBy != null) productFilter.setOrderBy(orderBy);

        return ResponseEntity.ok(productService.listProductsByFilter(productFilter, page, size));
    }

    @Operation(
            tags = {"Gestor Productos"},
            operationId = "updateProducto",
            summary = "Modifcar un producto",
            description = "Modifica los datos de un producto\n" +
                    "\n\n"+ Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Producto a modificar"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto modificado", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductDTO createProductDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, createProductDTO));
    }

    @Operation(
            tags = {"Gestor Productos"},
            operationId = "productStockIncrement",
            summary = "Incrementa el stock de un producto",
            description = "Incrementa el stock de un producto\n" +
                    "\n\n" + Constants.BADGED_EMPLOYEE +"\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock incrementado", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_EMPLEADO', 'ROLE_ADMINISTRADOR')")
    @PatchMapping("{id}/stock")
    public ResponseEntity<ProductDTO> addStockForProduct(@PathVariable Long id, @RequestParam(defaultValue = "0") int quantity) {
        return ResponseEntity.ok(productService.addStockForProduct(id, quantity));
    }

    @Operation(
            tags = {"Gestor Productos"},
            operationId = "productStockIncrement",
            summary = "Elimina un producto",
            description = "Eliminar un producto. **Esta operacion no puede ser realizada si existen ordenes antiguas/pendientes con este producto asignado**\n" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto eliminado", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @DeleteMapping("{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
