package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.security.*;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;

public interface AuthService {

    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    EmployeeDTO registerEmployee(RegisterEmployeeRequest request);
    void changepassword(ChangePasswordRequest request);

}
