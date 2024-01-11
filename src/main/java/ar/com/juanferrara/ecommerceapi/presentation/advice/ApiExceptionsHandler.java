package ar.com.juanferrara.ecommerceapi.presentation.advice;

import ar.com.juanferrara.ecommerceapi.domain.dto.ExceptionDto;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.GenericException;
import ar.com.juanferrara.ecommerceapi.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionsHandler {

    @ExceptionHandler(value = {GenericException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleGenericException(GenericException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ExceptionDto(exception.getHttpStatus().value(), exception.getError(), exception.getMessage()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDto(HttpStatus.NOT_FOUND.value(), "NOT FOUND", exception.getMessage()));
    }
}
