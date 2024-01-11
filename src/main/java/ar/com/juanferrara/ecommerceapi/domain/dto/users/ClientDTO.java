package ar.com.juanferrara.ecommerceapi.domain.dto.users;

import ar.com.juanferrara.ecommerceapi.domain.dto.address.AddressDTO;
import ar.com.juanferrara.ecommerceapi.domain.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClientDTO {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "22000111")
    private Long dni;

    @Email(message = "The mail must be valid")
    @Schema(example = "test@gmail.com")
    private String email;

    @NotBlank(message = "The names can't be blank")
    @Size(min =3, max = 60, message = "The names size must be between 3 and 60 characters")
    @Schema(example = "Name")
    private String names;

    @NotBlank(message = "The last name can't be blank")
    @Size(min =3, max = 60, message = "The last name size must be between 3 and 45 characters")
    @Schema(example = "Lastname")
    private String lastName;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Invalid date format")
    @Past(message = "The date must be in the past")
    @Schema(example = "01/11/1998")
    private Date birthDate;

    @NotNull(message = "The address can't be null")
    private Set<AddressDTO> addresses;
}
