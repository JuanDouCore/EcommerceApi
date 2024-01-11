package ar.com.juanferrara.ecommerceapi.persistence;

import ar.com.juanferrara.ecommerceapi.domain.entity.OrderDispatched;
import ar.com.juanferrara.ecommerceapi.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDispatchedRepository extends JpaRepository<OrderDispatched, Long>,  PagingAndSortingRepository<OrderDispatched, Long> {
    Optional<OrderDispatched> findByOrderId(Long orderId);
    Page<OrderDispatched> findAllByEmployeeDni(Long dniEmployee, Pageable pageable);
    Page<OrderDispatched> findAllByOrderClientDni(Long dniClient, Pageable pageable);
}
