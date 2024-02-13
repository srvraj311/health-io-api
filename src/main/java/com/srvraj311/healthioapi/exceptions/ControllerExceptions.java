package com.srvraj311.healthioapi.exceptions;

import org.springframework.http.HttpStatus;

public class ControllerExceptions {
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class OtpInvalidException extends RuntimeException {
        public OtpInvalidException(String message) {
            super(message);
        }
    }

    public static class DataAlreadyExistsException extends RuntimeException {
        public DataAlreadyExistsException(String message) {
            super(message);
        }
    }



    // TODO : Define other custom exception classes as needed
}
