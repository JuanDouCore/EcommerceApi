package ar.com.juanferrara.ecommerceapi.business.service.security;

import ar.com.juanferrara.ecommerceapi.business.service.RefreshTokenService;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.RefreshTokenRequest;
import ar.com.juanferrara.ecommerceapi.domain.dto.security.RefreshTokenResponse;
import ar.com.juanferrara.ecommerceapi.domain.entity.RefreshToken;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.persistence.RefreshTokenRepository;
import ar.com.juanferrara.ecommerceapi.persistence.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${security.refreshToken.expiration-minutes}")
    private long refreshTokenExpirationMinutes;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public RefreshToken generateRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findById(userId).get())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMinutes * 60 * 1000))
                .token(UUID.randomUUID().toString())
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::validateExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwt = jwtService.generateToken(user);
                    return RefreshTokenResponse.builder()
                            .tokenType("Bearer")
                            .token(jwt)
                            .refreshToken(requestRefreshToken)
                            .build();
                }).orElseThrow(() -> new GenericException("TOKEN INVALID", "token " + refreshTokenRequest + " invalid. Login again", HttpStatus.FORBIDDEN));
    }

    private RefreshToken validateExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new GenericException("TOKEN EXPIRED", "token " + refreshToken.getToken() + " expired. Login again", HttpStatus.FORBIDDEN);
        }
        return refreshToken;
    }
}
