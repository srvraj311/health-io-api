package com.srvraj311.healthioapi.service;

import com.srvraj311.healthioapi.dto.*;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.UserValidationService;
import com.srvraj311.healthioapi.models.OTP;
import com.srvraj311.healthioapi.models.User;
import com.srvraj311.healthioapi.repository.OTPValidationRepository;
import com.srvraj311.healthioapi.repository.UserRepository;
import com.srvraj311.healthioapi.schedules.DeleteOTP;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import com.srvraj311.healthioapi.utils.EmailConfig;
import com.srvraj311.healthioapi.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    final UserValidationService userValidationService;
    final UserRepository userRepository;
    final JwtTokenUtil jwtTokenUtil;
    final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final OTPValidationRepository oTPValidationRepository;
    private final DeleteOTP deleteOTP;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<ApiResponse> signUp(User user) {
        userValidationService.validateNotNull(user, "User");
        userValidationService.validateNotNull(user.getEmail(), "Email");
        user.setEmail(user.getEmail().toLowerCase());
        userValidationService.validateNotNull(user.getPassword(), "Password");
        userValidationService.validateNotNull(user.getRole(), "Role");
        userValidationService.validateEmailFormat(user.getEmail());
        userValidationService.validatePasswordStrength(user.getPassword());
        userValidationService.validateUserNotExistsByEmail(user.getEmail());
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Create a JWT token
        String token = jwtTokenUtil.generateToken(user);
        userRepository.insert(user);
        ResponseMap response = ResponseMap.builder().build();
        response.put("token", token);
        response.put("user", user);
        response.put("message", "User created successfully");
        return ResponseEntity.ok().body(ApiResponse.<ResponseMap>builder().status(Constants.OK).body(response).build());
    }

    private boolean verifyOtpFromDB(String email, String otp) {
        Optional<OTP> otpOptional = oTPValidationRepository.findByEmail(email);
        if (otpOptional.isPresent()) {
            OTP otpDB = otpOptional.get();
            return otpDB.getEmail().equals(email) && otp.equalsIgnoreCase(otpDB.getOtp()) &&
                    Long.parseLong(otpDB.getExpires_at()) > new Date(System.currentTimeMillis()).getTime();
        }
        return false;
    }

    public String generateToken(User user) {
        return jwtTokenUtil.generateToken(user);
    }

    public ResponseEntity<ApiResponse> update(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        userValidationService.validateNotNull(user, "User");
        userValidationService.validateNotNull(user.getEmail(), "Email");
        userValidationService.validateNotNull(user.getPassword(), "Password");
        userValidationService.validateNotNull(user.getRole(), "Role");
        userValidationService.validateEmailFormat(user.getEmail());
        userValidationService.validatePasswordStrength(user.getPassword());
        userValidationService.validateUserNotExistsByEmail(user.getEmail());
        Optional<User> oldUser = userRepository.findByEmail(user.getEmail());
        if (oldUser.isPresent()) {
            User u1 = oldUser.get();
            u1.setAddress(user.getAddress());
            u1.setAge(user.getAge());
            u1.setCity(user.getCity());
            u1.setCountry(user.getCountry());
            u1.setDob(user.getDob());
            u1.setGender(user.getGender());
            u1.setPincode(user.getPincode());
            u1.setBlood_group(user.getBlood_group());
            u1.setFirst_name(user.getFirst_name());
            u1.setLast_name(user.getLast_name());
            u1.setMobile_num(user.getMobile_num());
            userRepository.save(user);
            ResponseEntity.ok().body(ApiResponse.builder().body(user).status(Constants.OK).build());
        }

        throw new UsernameNotFoundException("User details not found on server");
    }

    public ResponseEntity<ApiResponse> sendNewOtp(String email, String command) throws InterruptedException {
        email = email.toLowerCase();
        userValidationService.validateNotNull(email, "Email");
        userValidationService.validateNotNull(command, "Command");
//      userValidationService.validateUserNotExistsByEmail(email); Because checking user is not required while sending otp
        userValidationService.validateEmailFormat(email);
        return sendMail(email, command);
    }

    public ResponseEntity<ApiResponse> sendMail(String email, String command) throws InterruptedException {
        int otp = (int) (Math.random() * 1000000);
        OTP otpDto = new OTP(email, String.valueOf(otp), String.valueOf(new Date(System.currentTimeMillis()).getTime() + 1000 * 60 * 10));
        oTPValidationRepository.save(otpDto);
        ResponseMap response = ResponseMap.builder().build();

        if (command.equalsIgnoreCase(Constants.CMD_OTP_SIGNUP)) {
            userValidationService.validateUserNotExistsByEmail(email);
        } else if (command.equalsIgnoreCase(Constants.CMD_OTP_FORGOT_PASSWORD)) {
            userValidationService.validateUserExistsByEmail(email);
        }

        Thread emailThread = new Thread(() -> {
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setEmailTo(email);
            emailConfig.setOtp(String.valueOf(otp));
            SimpleMailMessage message = switch (command) {
                case Constants.CMD_OTP_SIGNUP -> emailConfig.verificationTemplate();
                case Constants.CMD_OTP_FORGOT_PASSWORD -> emailConfig.emailTemplate();
                default -> emailConfig.emailTemplate();
            };
            mailSender.send(message);
            // Schedule auto delete OTP after 10 minutes
            deleteOTP.setOtp(otpDto);
            deleteOTP.run();
        });

        emailThread.start();
        emailThread.join(5000);

        response.put("message", "OTP Sent successfully");
        return ResponseEntity.ok().body(ApiResponse.<ResponseMap>builder().status(Constants.OK).body(response).build());
    }

    public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {
        userValidationService.validateNotNull(loginRequest, "Login Request");
        userValidationService.validateNotNull(loginRequest.getEmail(), "Email");
        userValidationService.validateNotNull(loginRequest.getPassword(), "Password");
        userValidationService.validateEmailFormat(loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            User user = new User();
            user.setEmail(loginRequest.getEmail());
            user.setPassword(loginRequest.getPassword());
            user.setRole(loginRequest.getRole());
            String token = generateToken(user);
            ResponseMap response = ResponseMap.builder().build();
            response.put("message", "Login Successful");
            response.put("token", token);
            return ResponseEntity.ok().body(ApiResponse.<ResponseMap>builder().status(Constants.OK).body(response).build());
        } else {
            throw new ControllerExceptions.UnauthorizedException("Invalid username or password");
        }
    }

    public ResponseEntity<ApiResponse> resetPassword(ResetPasswordRequest request) {
        request.setEmail(request.getEmail().toLowerCase());
        userValidationService.validateNotNull(request, "Request");
        userValidationService.validateNotNull(request.getEmail(), "Email");
        userValidationService.validateNotNull(request.getPassword(), "Password");
        userValidationService.validatePasswordStrength(request.getPassword());
        userValidationService.validateEmailFormat(request.getEmail());
        userValidationService.validateUserExistsByEmail(request.getEmail());

        Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
        if (dbUser.isPresent()) {
            dbUser.get().setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(dbUser.get());
            return ResponseEntity.ok(ApiResponse.builder().status(Constants.OK).body(AppUtil.getEmptyMap("Login Successful")).build());
        }

        throw new ControllerExceptions.OtpInvalidException("OTP invalid or expired");
    }

    public ResponseEntity<ApiResponse> logout() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return ResponseEntity.ok().body(ApiResponse.builder().status(Constants.OK).body(AppUtil.getEmptyMap("Logout Successful")).build());
        }

        throw new ControllerExceptions.BadRequestException("Not logged in");
    }

    public ResponseEntity<ApiResponse<Object>> validateUser(String email) {
        userValidationService.validateNotNull(email, "Email");
        userValidationService.validateEmailFormat(email);
        email = email.toLowerCase();
        ResponseMap response = ResponseMap.builder().build();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            response.put("message", "User exists");
        } else {
            throw new UsernameNotFoundException("User not found");
        }
        return ResponseEntity.ok().body(ApiResponse.builder().status(Constants.OK).body(response).build());
    }

    public ResponseEntity<ApiResponse> validate() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            ResponseMap response = ResponseMap.builder().build();
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails userDetails) {
                response.put("message", "User authenticated successfully");
                User user = userRepository.findByEmail(userDetails.getUsername()).isPresent() ? userRepository.findByEmail(userDetails.getUsername()).get() : null;
                // Remove password field from user object
                user.setPassword(null);
                response.put("user", user);
                return ResponseEntity.ok().body(ApiResponse.<ResponseMap>builder().status(Constants.OK).body(response).build());
            }
        }
        throw new ControllerExceptions.UnauthorizedException("User not authorized");
    }

    public ResponseEntity<ApiResponse> verifyOtp(String email, String otp) {
        userValidationService.validateNotNull(email, "Email");
        userValidationService.validateNotNull(otp, "OTP");
        userValidationService.validateEmailFormat(email);
        email = email.toLowerCase();

        if (verifyOtpFromDB(email, otp)) {
            return ResponseEntity.ok().body(ApiResponse.<ResponseMap>builder().status(Constants.OK).body(AppUtil.getEmptyMap("OTP verified")).build());
        } else {
            throw new ControllerExceptions.OtpInvalidException("OTP invalid or expired");
        }
    }
}
