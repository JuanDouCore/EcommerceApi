package ar.com.juanferrara.ecommerceapi.business.mapper.user;

import ar.com.juanferrara.ecommerceapi.business.mapper.GenericMapper;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Client;
import ar.com.juanferrara.ecommerceapi.domain.entity.Employee;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployeeDtoMapper {

    @Mapping(target = "userId", source = "employee.user.id")
    @Mapping(target = "email", source = "employee.user.email")
    @Mapping(target = "role", source = "employee.user.role")
    EmployeeDTO toEmployeDto(Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dni" , ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "names", source = "source.names")
    @Mapping(target = "lastName", source = "source.lastName")
    Employee updateEmployee(Employee target, Employee source);

    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "userId", source = "employee.user.id")
    @Mapping(target = "email", source = "employee.user.email")
    @Mapping(target = "role", source = "employee.user.role")
    EmployeeDTO toDto(Employee employee);
}
