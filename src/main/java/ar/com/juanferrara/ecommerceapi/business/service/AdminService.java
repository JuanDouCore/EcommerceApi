package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.UserDTO;

public interface AdminService {

    UserDTO changeUserRole(Long userId);


}
