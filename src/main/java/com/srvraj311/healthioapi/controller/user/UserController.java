package com.srvraj311.healthioapi.controller.user;

import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.models.SignupRequestWithOtp;
import com.srvraj311.healthioapi.models.User;
import com.srvraj311.healthioapi.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public HashMap<String, String> getUser() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Server", "Working");
        return map;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = userService.generateToken(user);
            HashMap<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "User authenticated successfully");
            return ResponseEntity.ok().body(response);
        } else {
            throw new ControllerExceptions.BadRequestException("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignupRequestWithOtp user) {
        if (user == null) {
            throw new ControllerExceptions.BadRequestException("User object is null");
        }
        return userService.signUp(user);
    }

    @PostMapping("update")
    public ResponseEntity<Object> update(@RequestBody User user) {
        if (user == null) {
            throw new ControllerExceptions.BadRequestException("User object is null");
        }
        return userService.update(user);
    }

    // Test endpoint to decrypt token
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity.status(200).body(
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            );
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @PostMapping("/send_otp")
    public ResponseEntity<Object> sendOtp(@Param("email") String email) throws InterruptedException {
        return userService.sendnewOtp(email);
    };
}
