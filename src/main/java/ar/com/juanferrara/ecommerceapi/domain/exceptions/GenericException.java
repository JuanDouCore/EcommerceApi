package ar.com.juanferrara.ecommerceapi.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException{

    @Getter
    private String error;

    @Getter
    private HttpStatus httpStatus;

    public GenericException(String error, String message, HttpStatus  httpStatus) {
        super(message);
        this.error = error;
        this.httpStatus = httpStatus;
    }
}
