package com.srvraj311.healthioapi.service;

import com.google.gson.Gson;
import com.srvraj311.healthioapi.dto.LoginRequest;
import com.srvraj311.healthioapi.dto.ResetPasswordRequest;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.UserValidationService;
import com.srvraj311.healthioapi.models.OTP;
import com.srvraj311.healthioapi.dto.SignupRequestWithOtp;
import com.srvraj311.healthioapi.models.User;
import com.srvraj311.healthioapi.repository.OTPValidationRepository;
import com.srvraj311.healthioapi.repository.UserRepository;
import com.srvraj311.healthioapi.schedules.DeleteOTP;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import com.srvraj311.healthioapi.utils.EmailConfig;
import com.srvraj311.healthioapi.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    final UserValidationService userValidationService;
    final UserRepository userRepository;
    final JwtTokenUtil jwtTokenUtil;
    final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final OTPValidationRepository oTPValidationRepository;
    private final DeleteOTP deleteOTP;
    private final AuthenticationManager authenticationManager;

    public UserService(UserValidationService userValidationService, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, JavaMailSender mailSender,
                       OTPValidationRepository oTPValidationRepository, DeleteOTP deleteOTP, AuthenticationManager authenticationManager) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.oTPValidationRepository = oTPValidationRepository;
        this.deleteOTP = deleteOTP;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<Object> signUp(SignupRequestWithOtp user) {
        try {
            userValidationService.validateNotNull(user, "User");
            userValidationService.validateNotNull(user.getOtp(), "OTP");
            userValidationService.validateNotNull(user.getEmail(), "Email");
            user.setEmail(user.getEmail().toLowerCase());
            // verify OTP to signup
            boolean isOtpVerified = verifyOtpFromDB(user.getEmail(), user.getOtp());
            if (!isOtpVerified) {
                throw new ControllerExceptions.OtpInvalidException("Otp did not match");
            }
            userValidationService.validateNotNull(user.getPassword(), "Password");
            userValidationService.validateNotNull(user.getRole(), "Role");
            userValidationService.validateEmailFormat(user.getEmail());
            userValidationService.validatePasswordStrength(user.getPassword());
            userValidationService.validateUserNotExistsByEmail(user.getEmail());
            // Encrypt password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Create a JWT token
            String token = jwtTokenUtil.generateToken(user);
            HashMap<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("user", new Gson().toJson(user));
            response.put("message", "User created successfully");
            userRepository.insert(user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private boolean verifyOtpFromDB(String email, String otp) {
        Optional<OTP> otpOptional = oTPValidationRepository.findByEmail(email);
        if (otpOptional.isPresent()) {
            OTP otpDB = otpOptional.get();
            return  otpDB.getEmail().equals(email) &&
                Long.parseLong(otpDB.getExpires_at()) > new Date(System.currentTimeMillis()).getTime();
        }
        return false;
    }

    public String generateToken(User user) {
        return jwtTokenUtil.generateToken(user);
    }

    public ResponseEntity<Object> update(User user) {
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
            return ResponseEntity.ok().body(userRepository.save(user));
        }

        throw new UsernameNotFoundException("User details not found on server");
    }

    public ResponseEntity<Object> sendNewOtp(String email , String command) throws InterruptedException {
        email = email.toLowerCase();
        userValidationService.validateNotNull( email, "Email");
        userValidationService.validateNotNull(command, "Command");
        userValidationService.validateUserNotExistsByEmail(email);
        userValidationService.validateEmailFormat(email);
        return sendMail(email, command);
    }

    public ResponseEntity<Object> sendMail(String email , String command) throws InterruptedException {
        int otp = (int) (Math.random() * 1000000);
        OTP otpDto = new OTP(email , String.valueOf(otp), String.valueOf(new Date(System.currentTimeMillis()).getTime() + 1000 * 60 * 10));
        oTPValidationRepository.save(otpDto);
        HashMap<String, String> response = new HashMap<>();

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
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Object> login(LoginRequest loginRequest) {
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
            HashMap<String, String> response = AppUtil.getEmptyMap("Login successful");
            response.put("token", token);
            return ResponseEntity.ok().body(response);
        } else {
            throw new ControllerExceptions.UnauthorizedException("Invalid username or password");
        }
    }

    public ResponseEntity<Object> resetPassword(ResetPasswordRequest request) {
        userValidationService.validateNotNull(request, "Request");
        userValidationService.validateNotNull(request.getEmail(), "Email");
        userValidationService.validateNotNull(request.getPassword(), "Password");
        userValidationService.validateNotNull(request.getOtp(), "OTP");
        userValidationService.validatePasswordStrength(request.getPassword());
        userValidationService.validateEmailFormat(request.getEmail());
        userValidationService.validateUserNotExistsByEmail(request.getEmail());

        Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
        if(dbUser.isPresent() && verifyOtpFromDB(request.getEmail(), request.getOtp())) {
            dbUser.get().setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(dbUser.get());
            return ResponseEntity.ok(AppUtil.getEmptyMap("Login Successful"));
        }

        throw new ControllerExceptions.OtpInvalidException("OTP invalid or expired");
    }

    public ResponseEntity<Object> logout() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return ResponseEntity.ok(AppUtil.getEmptyMap("Logout successful"));
        }

        throw new ControllerExceptions.BadRequestException("Not logged in");
    }
}
