package org.example.java5nsd20303.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice

public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomErrorDetails> handleAllException(Exception ex, WebRequest request) {
        CustomErrorDetails errorDetails =new CustomErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);

    }



    @ExceptionHandler(CustomResourceNotFoundException.class)
    public final ResponseEntity<CustomErrorDetails> handleResourceNotFoundException(Exception ex, WebRequest request) {
        CustomErrorDetails errorDetails =new CustomErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Quản lý lỗi
    //Dùng bên postbook controller
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<CustomErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        CustomErrorDetails errorDetails =new CustomErrorDetails(LocalDateTime.now(),
                "Access Denied: You do not have permision to access this resource ", request.getDescription(false));

                return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);

    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode statusCode,
                                                               WebRequest request) {

        String message = ex.getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(","));

                CustomErrorDetails errorDetails =new CustomErrorDetails(
                        LocalDateTime.now(),
                        message,
                        request.getDescription(false)
                );
        return new   ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
