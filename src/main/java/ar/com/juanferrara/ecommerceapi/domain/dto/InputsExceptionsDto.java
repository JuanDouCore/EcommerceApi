package ar.com.juanferrara.ecommerceapi.domain.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

public record InputsExceptionsDto(int code, String error, Map valueError) {
}
