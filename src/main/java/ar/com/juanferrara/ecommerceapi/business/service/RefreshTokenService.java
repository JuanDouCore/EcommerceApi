package ar.com.juanferrara.ecommerceapi.business.service;

import ar.com.juanferrara.ecommerceapi.domain.dto.security.RefreshTokenRequest;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.RefreshTokenResponse;
import ar.com.juanferrara.ecommerceapi.domain.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(Long userId);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
