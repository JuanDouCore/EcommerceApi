package ar.com.juanferrara.ecommerceapi.presentation.controller;

import ar.com.juanferrara.ecommerceapi.business.service.ProductCategoryService;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.CreateProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductCategoryDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.products.ProductDTO;
import ar.com.juanferrara.ecommerceapi.domain.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products/products-categories")

public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;


    @Operation(
            tags = {"Gestor Categorias de Productos"},
            operationId = "createProductCategory",
            summary = "Crear una categoria de productos",
            description = "Crea una categoria de productos para posteriormente asignarle productos a esta." +
                    "\n\n"+ Constants.BADGED_ADMIN,
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Categoria a crear"
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoria creada", content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))),
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@RequestBody @Valid CreateProductCategoryDTO createProductCategoryDTO) {
        ProductCategoryDTO productCategoryCreated = productCategoryService.createCategory(createProductCategoryDTO);
        return ResponseEntity.created(URI.create("/api/products/products-categories/" + productCategoryCreated.getId())).body(productCategoryCreated);
    }

    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "listAllProductsCategories",
            summary = "Listar categorias",
            description = "Listar todas las categorias."
    )
    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> listAllProductCategories() {
        return ResponseEntity.ok(productCategoryService.getAllCategories());
    }

    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "getCategoryById",
            summary = "Obtener una categoria por su ID",
            description = "Obtener una categoria por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria encontrada", content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria no encontrado", content = @Content)
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<ProductCategoryDTO> getProductCategory(@PathVariable  Long id) {
        return ResponseEntity.ok(productCategoryService.getCategoryById(id));
    }

    @Operation(
            tags = {"Vistas Categorias y Productos"},
            operationId = "getCategoryByName",
            summary = "Obtener una categoria por su nombre",
            description = "Obtener una categoria por su nombre.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria encontrada", content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria no encontrado", content = @Content)
            }
    )
    @GetMapping("{name}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(productCategoryService.getCategoryByName(name));
    }

    @Operation(
            tags = {"Gestor Categorias de Productos"},
            operationId = "updateCategory",
            summary = "Modifcar una categoria",
            description = "Modifica los datos de una categoria" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Producto a modificar"
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria modificada", content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PutMapping("{id}")
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(@PathVariable Long id, @RequestBody @Valid CreateProductCategoryDTO createProductCategoryDTO) {
        return ResponseEntity.ok(productCategoryService.updateCategory(id, createProductCategoryDTO));
    }

    @Operation(
            tags = {"Gestor Categorias de Productos"},
            operationId = "deleteCategory",
            summary = "Eliminar una categoria",
            description = "Elimina una categoria. **Esta operacion no puede ser realizada si existen productos con esta categoria asociada**" +
                    "\n\n" + Constants.BADGED_ADMIN,
            security = {@SecurityRequirement(name = "Bearer Authentication")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria eliminada", content = @Content(schema = @Schema(implementation = ProductCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content)
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @DeleteMapping("{id}")
    public ResponseEntity<ProductCategoryDTO> deleteProductCategory(@PathVariable Long id) {
        ProductCategoryDTO productCategoryDTO = productCategoryService.deleteCategory(id);
        return ResponseEntity.ok(productCategoryDTO);
    }


}
