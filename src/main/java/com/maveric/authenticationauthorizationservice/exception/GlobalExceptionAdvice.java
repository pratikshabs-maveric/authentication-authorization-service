package com.maveric.authenticationauthorizationservice.exception;

import com.maveric.authenticationauthorizationservice.constant.ErrorMessageConstant;
import com.maveric.authenticationauthorizationservice.dto.Error;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> internalServerError(Exception exception){
        Error error = getError(exception.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Error> feignExceptionError(FeignException exception){
        Error error = getError(exception.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error>  handleInvalidInput(MethodArgumentNotValidException methodArgumentNotValidException){
        Error error = getError(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage(),String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Error>  handleInvalidPasswordOrEmail(BadCredentialsException badCredentialsException){
        Error error = getError(ErrorMessageConstant.EMAIL_PASSWORD_ERROR,String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
    public ResponseEntity<Error>  serviceUnavailable(HttpServerErrorException.ServiceUnavailable serviceUnavailable){
        Error error = getError(String.valueOf(serviceUnavailable.getMessage()),String.valueOf(HttpStatus.SERVICE_UNAVAILABLE));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(UserFeignException.class)
    public ResponseEntity<Error> handleUserFeignException(UserFeignException userFeignException){
        Error error = getError(userFeignException.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private Error getError(String message , String code){
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;

    }
}
