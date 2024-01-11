package ar.com.juanferrara.ecommerceapi;

import ar.com.juanferrara.ecommerceapi.business.service.AuthService;
import ar.com.juanferrara.ecommerceapi.business.service.EmployeeService;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.RegisterEmployeeRequest;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ECommerceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApiApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner commandLineRunner(AuthService authService) {
        return args -> {
            RegisterEmployeeRequest request = RegisterEmployeeRequest.builder()
                    .email("admin@admin.com")
                    .password("administrador123")
                    .dni(11222333L)
                    .names("Admin")
                    .lastName("Admin")
                    .build();
            authService.registerEmployee(request);
        };
    }*/
}
