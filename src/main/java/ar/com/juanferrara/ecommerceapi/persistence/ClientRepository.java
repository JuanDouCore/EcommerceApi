package ar.com.juanferrara.ecommerceapi.persistence;

import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long>, PagingAndSortingRepository<Client, Long>, QueryByExampleExecutor<Client> {
    Optional<Client> findByUser(User user);

    Optional<Client> findByUserEmail(String email);

    boolean existsByDniOrUserEmail(Long dni, String email);

    Optional<Client> findByUserId(Long clientUserId);
}
