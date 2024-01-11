package ar.com.juanferrara.ecommerceapi.config.security.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String responseBody = "{"
                + "\"code\": 401,"
                + "\"error\": \"NOT AUTHORIZED\","
                + "\"message\": \"" + accessDeniedException.getMessage() + "\""
                + "}";

        PrintWriter out = response.getWriter();
        out.write(responseBody);
        out.flush();
    }
}
