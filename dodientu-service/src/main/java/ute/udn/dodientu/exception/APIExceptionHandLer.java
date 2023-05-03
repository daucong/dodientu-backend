package ute.udn.dodientu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ute.udn.dodientu.dto.CustomDTO;

@RestControllerAdvice
public class APIExceptionHandLer {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomDTO handlerNotFoundException(NotFoundException ex, WebRequest req) {
        return new CustomDTO(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomDTO handlerValidateException(ApiException ex, WebRequest req) {
        return new CustomDTO(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomDTO handleBindException(BindException e) {
        StringBuilder error = new StringBuilder();
        e.getBindingResult().getFieldErrors()
                .stream()
                .forEach(f -> error.append(f.getField() + ": " + f.getDefaultMessage() + "."));
        return new CustomDTO(HttpStatus.BAD_REQUEST, error.toString());
    }
}