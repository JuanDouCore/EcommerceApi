package ar.com.juanferrara.ecommerceapi.business.service.impl;

import ar.com.juanferrara.ecommerceapi.business.mapper.PageResponseMapper;
import ar.com.juanferrara.ecommerceapi.business.mapper.user.EmployeeDtoMapper;
import ar.com.juanferrara.ecommerceapi.business.service.EmployeeService;
import ar.com.juanferrara.ecommerceapi.domain.dto.PageResponse;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.UserDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.EmployeeRepository;
import ar.com.juanferrara.ecommerceapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeDtoMapper employeeDtoMapper;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User userAssigned) {
        Employee employee = employeeDtoMapper.toEntity(employeeDTO);
        employee.setUser(userAssigned);

        return employeeDtoMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDTO getEmployeeByDni(Long dni) {
        Employee employee = employeeRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        return employeeDtoMapper.toEmployeDto(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        return employeeDtoMapper.toEmployeDto(employee);
    }

    @Override
    public PageResponse listAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return PageResponseMapper.convertToPageResponse(employeePage.map(employeeDtoMapper::toEmployeDto));
    }

    @Override
    public PageResponse findEmployees(Pageable pageable, EmployeeDTO employeeExample) {
        Employee employeeExampleToFind = employeeDtoMapper.toEntity(employeeExample);

        Example<Employee> exampleEmployee = Example.of(employeeExampleToFind, ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase());

        Page<Employee> pageEmployee = employeeRepository.findAll(exampleEmployee, pageable);
        return PageResponseMapper.convertToPageResponse(pageEmployee);
    }

    @Override
    public EmployeeDTO changeEmployeeRole(Long employeeDni, UserRole newRole) {
        Employee employee = employeeRepository.findById(employeeDni)
                .orElseThrow(() -> new NotFoundException("No employee found with this dni"));

        if(newRole != UserRole.CLIENTE) {
            employee.getUser().setRole(newRole);
        } else {
            throw new GenericException("ROLE INCORRECT", "Users registered as employees cannot have the client role", HttpStatus.BAD_REQUEST);
        }

        employeeRepository.save(employee);

        return employeeDtoMapper.toEmployeDto(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long dni, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        employeeDtoMapper.updateEmployee(employee, employeeDtoMapper.toEntity(employeeDTO));

        employeeRepository.save(employee);

        return employeeDtoMapper.toEmployeDto(employee);
    }

    @Override
    public EmployeeDTO disableEmployee(Long dni) {
        Employee employee = employeeRepository.findById(dni)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        employee.getUser().setUserEnabled(false);
        employeeRepository.save(employee);

        return employeeDtoMapper.toEmployeDto(employee);
    }
}
