package com.srvraj311.healthioapi.exceptions;

import com.srvraj311.healthioapi.utils.AppUtil;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler({ControllerExceptions.BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(ControllerExceptions.BadRequestException ex) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage())
        );
    }

    @ExceptionHandler({ControllerExceptions.NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage())
        );
    }

    @ExceptionHandler({ControllerExceptions.UnauthorizedException.class})
    public  ResponseEntity<Object> handleUnauthorizedException(ControllerExceptions.UnauthorizedException ex) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage())
        );
    }
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage())
        );
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<Object> handlePathvariableMissingException(MissingPathVariableException e) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())
        );
    }

    @ExceptionHandler({ControllerExceptions.OtpInvalidException.class})
    public ResponseEntity<Object> handleotpInvalidException(ControllerExceptions.OtpInvalidException exception) {
        return AppUtil.getErrorObjectFromResponseEntity(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage())
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        return AppUtil.getErrorObjectFromResponseEntity(
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage())
        );
    }







    // Add handlers for other custom exceptions

    // Add a default handler for all other exceptions
//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//    }

}
