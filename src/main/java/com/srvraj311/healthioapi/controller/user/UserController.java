package com.srvraj311.healthioapi.controller.user;

import com.srvraj311.healthioapi.dto.*;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.models.User;
import com.srvraj311.healthioapi.service.UserService;
import com.srvraj311.healthioapi.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignupRequestWithOtp user) {
        return userService.signUp(user);
    }

    @PostMapping("update")
    public ResponseEntity<ApiResponse> update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * @param email   for sending email
     * @param command takes command Constants.CMD_OTP_FORGOT_PASSWORD, Constants.CMD_OTP_SIGNUP
     * @return ResponseEntity<Object>
     * @throws InterruptedException
     */
    @PostMapping("/send_otp")
    public ResponseEntity<ApiResponse> sendOtp(@Param("email") String email, @Param("command") String command) throws InterruptedException {
        return userService.sendNewOtp(email, command);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> login(@RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return userService.logout();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = userService.generateToken(user);
            HashMap<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "User authenticated successfully");
            return ResponseEntity.ok().body(ApiResponse.builder().status(Constants.OK).body(response).build());
        } else {
            throw new ControllerExceptions.BadRequestException("Invalid username or password");
        }
    }

    // Test endpoint to decrypt token
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getUsers() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            ResponseMap responseMap = ResponseMap.builder().build();
            responseMap.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(responseMap)
                            .build()
            );
        }
        throw new ControllerExceptions.UnauthorizedException("User not authorized");
    }
}
