package ar.com.juanferrara.ecommerceapi.business.service.security;

import ar.com.juanferrara.ecommerceapi.business.service.AuthService;
import ar.com.juanferrara.ecommerceapi.business.service.ClientService;
import ar.com.juanferrara.ecommerceapi.business.service.EmployeeService;
import ar.com.juanferrara.ecommerceapi.business.service.RefreshTokenService;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.*;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.ClientDTO;
import ar.com.juanferrara.ecommerceapi.domain.dto.users.EmployeeDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import ar.com.juanferrara.ecommerceapi.domain.enums.UserRole;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.ClientRepository;
import ar.com.juanferrara.ecommerceapi.persistence.EmployeeRepository;
import ar.com.juanferrara.ecommerceapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ClientService clientService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;


    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public LoginResponse login(LoginRequest request) {
        if(!userRepository.existByEmail(request.getEmail()))
            throw new NotFoundException("User not found");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword());

        authenticationProvider.authenticate(usernamePasswordAuthenticationToken);

        User user = userRepository.findByEmail(request.getEmail()).get();
        LoginResponse<?> response;

        String jwt = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(user.getId()).getToken();

        if(user.getRole().equals(UserRole.CLIENTE))
            response = new LoginResponse<ClientDTO>(
                    clientService.getClientByEmail(user.getEmail()),
                    "Bearer",
                    jwt,
                    refreshToken
            );
        else
            response = new LoginResponse<EmployeeDTO>(
                    employeeService.getEmployeeByEmail(user.getEmail()),
                    "Bearer",
                    jwt,
                    refreshToken
            );

        return response;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        if(userRepository.existByEmail(request.getEmail()) || clientRepository.existsById(request.getDni()))
            throw new GenericException("CONFLICT REGISTER USER", "Already exist user by email or dni", HttpStatus.CONFLICT);

        Date birthDate;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            birthDate = format.parse(request.getBirthDate());
        } catch (ParseException e) {
            throw new GenericException("BAD REQUEST REGISTER", "Invalid date format (dd/MM/yyyy)", HttpStatus.BAD_REQUEST);
        }

        ClientDTO clientForRegister = ClientDTO.builder()
                .dni(request.getDni())
                .names(request.getNames())
                .lastName(request.getLastName())
                .birthDate(birthDate)
                .addresses(Set.of(request.getAddress()))
                .build();

        User userCreated = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CLIENTE)
                .userEnabled(true)
                .build());

        String jwt = jwtService.generateToken(userCreated);
        String refreshToken = refreshTokenService.generateRefreshToken(userCreated.getId()).getToken();

        ClientDTO clientDTO = clientService.createClient(clientForRegister, userCreated);

        return new LoginResponse<>(
                clientDTO,
                "Bearer",
                jwt,
                refreshToken
        );
    }

    @Override
    public EmployeeDTO registerEmployee(RegisterEmployeeRequest request) {
        if(userRepository.existByEmail(request.getEmail()) || employeeRepository.existsById(request.getDni()))
            throw new GenericException("CONFLICT REGISTER USER", "Already exist user by email or dni", HttpStatus.CONFLICT);

        EmployeeDTO employeeForRegister = EmployeeDTO.builder()
                .dni(request.getDni())
                .names(request.getNames())
                .lastName(request.getLastName())
                .build();

        User userCreated = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.EMPLEADO)
                .userEnabled(true)
                .build());

        return  employeeService.createEmployee(employeeForRegister, userCreated);
    }

    @Override
    public void changepassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(user.getPassword().equals(passwordEncoder.encode(request.getCurrentPassword())))
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        else
            throw new GenericException("BAD REQUEST CHANGE PASSWORD", "Invalid old password", HttpStatus.BAD_REQUEST);

        userRepository.save(user);
    }
}
