package ar.com.juanferrara.ecommerceapi.config.security.filters;

import ar.com.juanferrara.ecommerceapi.business.service.security.JwtService;
import ar.com.juanferrara.ecommerceapi.domain.entity.User;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import ar.com.juanferrara.ecommerceapi.persistence.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.split(" ")[1];

        if (!isValidToken(jwt, response))
            return;

        String email = jwtService.extractEmail(jwt);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isValidToken(String jwt, HttpServletResponse response) throws IOException {
        boolean isValidToken = false;
        try {
            jwtService.validateToken(jwt);
            isValidToken = true;
        } catch (ExpiredJwtException e) {
            catchResponseValidateException(response, "expired token");
        } catch (SignatureException e) {
            catchResponseValidateException(response, "invalid sign token");
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            catchResponseValidateException(response, "malformed token");
        }  catch (Exception e) {
            catchResponseValidateException(response, e.getMessage());
        }

        return isValidToken;
    }

    private void catchResponseValidateException(HttpServletResponse response, String exceptionReason) throws IOException {
        String responseBody = "{"
                + "\"code\": 401,"
                + "\"error\": \"NOT AUTHORIZED\","
                + "\"message\": \"" + exceptionReason + "\""
                + "}";

        PrintWriter out = response.getWriter();
        out.write(responseBody);
        out.flush();
    }
}
