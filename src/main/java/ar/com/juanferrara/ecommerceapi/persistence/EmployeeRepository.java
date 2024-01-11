package ar.com.juanferrara.ecommerceapi.persistence;

import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, PagingAndSortingRepository<Employee, Long>, QueryByExampleExecutor<Employee> {
    Optional<Employee> findByUser(User user);
    Optional<Employee> findByUserEmail(String email);

    boolean existsByDniOrUserEmail(Long dni, String email);

}
