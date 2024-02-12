package com.srvraj311.healthioapi.exceptions;

import com.srvraj311.healthioapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidationService {
        @Autowired
        private UserRepository userRepository; // Assuming UserRepository is your repository for user data

        public void validateNotNull(Object obj, String fieldName) {
            if (obj == null) {
                throw new ControllerExceptions.BadRequestException(fieldName + " cannot be null");
            }
        }

        public void validateUserNotExistsByEmail(String email) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new ControllerExceptions.BadRequestException("User with email " + email + " already exists");
            }
        }

        public void validateEmailFormat(String email) {
            if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", email)) {
                throw new ControllerExceptions.BadRequestException("Invalid email format");
            }
        }

        public void validatePasswordStrength(String password) {
            // Implement password strength validation logic (e.g., minimum length, special characters, etc.)
            if (password.length() < 8) {
                throw new ControllerExceptions.BadRequestException("Password must be at least 8 characters long");
            }
            // TODO : Add other password strength checks as needed
        }

    // TODO : Add more validation methods as per your requirements
}
