package ar.com.juanferrara.ecommerceapi.config.security.exceptions;

import ar.com.juanferrara.ecommerceapi.domain.dto.ExceptionDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String responseBody = "{"
                + "\"code\": 401,"
                + "\"error\": \"NOT AUTHORIZED\","
                + "\"message\": \"" + authException.getMessage() + "\""
                + "}";

        PrintWriter out = response.getWriter();
        out.write(responseBody);
        out.flush();
    }
}
