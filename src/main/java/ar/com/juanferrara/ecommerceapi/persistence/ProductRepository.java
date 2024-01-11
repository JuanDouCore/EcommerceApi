package ar.com.juanferrara.ecommerceapi.persistence;

import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import ar.com.juanferrara.ecommerceapi.domain.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long>, QueryByExampleExecutor<Product> {
    boolean existsByCategory(ProductCategory productCategory);
}
