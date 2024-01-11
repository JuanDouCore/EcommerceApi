package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.UserDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User userAssigned);

    EmployeeDTO getEmployeeByDni(Long dni);

    EmployeeDTO getEmployeeByEmail(String email);
    PageResponse listAllEmployees(Pageable pageable);
    PageResponse findEmployees(Pageable pageable, EmployeeDTO employeeExample);
    EmployeeDTO changeEmployeeRole(Long employeeDni, UserRole newRole);
    EmployeeDTO updateEmployee(Long dni, EmployeeDTO employeeDTO);

    EmployeeDTO disableEmployee(Long dni);
}
