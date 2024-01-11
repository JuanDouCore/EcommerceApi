package ar.com.juanferrara.ecommerceapi.presentation.advice;


import ar.com.juanferrara.ecommerceapi.domain.dto.ExceptionDto;
import ar.com.juanferrara.ecommerceapi.domain.dto.InputsExceptionsDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GenericsExceptionsHandler {



    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "REQUEST METHOD NOT SUPPORTED", "Methods supported for this resource " + Arrays.toString(exception.getSupportedMethods())));
    }

    @ExceptionHandler(value = {MissingServletRequestPartException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "MULTIPART REQUEST INVALID", exception.getMessage()));
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleMultipartException(MultipartException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "MULTIPART REQUEST INVALID", exception.getMessage()));
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleNoResourceFoundException(NoResourceFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDto(HttpStatus.NOT_FOUND.value(), "RESOURCE NOT FOUND", exception.getResourcePath() + " not found resource. Check api docs"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InputsExceptionsDto> handleMethodArgumentException(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        InputsExceptionsDto  inputsExceptionsDto = new InputsExceptionsDto(HttpStatus.BAD_REQUEST.value(), "INVALID ARGUMENT", errorMap);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(inputsExceptionsDto);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "MISS PARAM", "miss param key=" + exception.getParameterName() + " name=" + exception.getParameterType()));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleValidationConstraintException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), exception.getLocalizedMessage(), exception.getMessage()));
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "MEDIATYPE NOT SUPPORTED", exception.getMessage()));
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ExceptionDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), "MESSAGE NOT Readable", exception.getMessage()));
    }
}
