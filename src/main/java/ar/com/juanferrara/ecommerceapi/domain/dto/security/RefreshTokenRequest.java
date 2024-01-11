package ar.com.juanferrara.ecommerceapi.domain.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @Schema(example = "02393d98-1739-4451-a3c5-fc6507db46e7")
    private String refreshToken;
}
