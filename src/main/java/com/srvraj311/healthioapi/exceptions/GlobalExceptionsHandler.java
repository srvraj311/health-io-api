package com.srvraj311.healthioapi.exceptions;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ErrorBody;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.srvraj311.healthioapi.utils.Constants.FAILED;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler({ControllerExceptions.BadRequestException.class})
    public ResponseEntity<ApiResponse> handleBadRequestException(ControllerExceptions.BadRequestException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ControllerExceptions.NotFoundException.class})
    public ResponseEntity<ApiResponse> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ControllerExceptions.UnauthorizedException.class})
    public  ResponseEntity<ApiResponse> handleUnauthorizedException(ControllerExceptions.UnauthorizedException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<ApiResponse> handlePathvariableMissingException(MissingPathVariableException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ControllerExceptions.OtpInvalidException.class})
    public ResponseEntity<ApiResponse> handleotpInvalidException(ControllerExceptions.OtpInvalidException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ControllerExceptions.DataAlreadyExistsException.class})
    public ResponseEntity<ApiResponse> handleDataAlreadyExistsException(Exception ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody("You are not allowed to access this resource", ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> handleRuntimeException(Exception ex) {
        ApiResponse apiResponse = ApiResponse.builder()
                .status(FAILED)
                .error(new ErrorBody(ex.getMessage(), ex.getClass().getSimpleName()))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }






    // Add handlers for other custom exceptions

    // Add a default handler for all other exceptions
//    @ExceptionHandler({Exception.class})
//    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//    }

}
