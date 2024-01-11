package ar.com.juanferrara.ecommerceapi.persistence;

import ar.com.juanferrara.ecommerceapi.domain.dto.order.OrderDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Order;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import ar.com.juanferrara.ecommerceapi.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {
    List<Order> findAllByClientUserId(Long userId);

    Optional<Order> findByOrderReferenceExternal(String purcharseReference);

    List<Order> findAllByStatus(OrderStatus status);
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findAllByClientUserId(Long userId, Pageable pageable);
}
