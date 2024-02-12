package com.srvraj311.healthioapi.service;

import com.google.gson.Gson;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.UserValidationService;
import com.srvraj311.healthioapi.models.OTP;
import com.srvraj311.healthioapi.models.SignupRequestWithOtp;
import com.srvraj311.healthioapi.models.User;
import com.srvraj311.healthioapi.repository.OTPValidationRepository;
import com.srvraj311.healthioapi.repository.UserRepository;
import com.srvraj311.healthioapi.schedules.DeleteOTP;
import com.srvraj311.healthioapi.utils.EmailConfig;
import com.srvraj311.healthioapi.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    public UserService(UserValidationService userValidationService, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, JavaMailSender mailSender,
                       OTPValidationRepository oTPValidationRepository, DeleteOTP deleteOTP) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.oTPValidationRepository = oTPValidationRepository;
        this.deleteOTP = deleteOTP;
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

    public ResponseEntity<Object> sendnewOtp(String email) throws InterruptedException {
        email = email.toLowerCase();
        userValidationService.validateNotNull( email, "Email");
        userValidationService.validateUserNotExistsByEmail(email);
        userValidationService.validateEmailFormat(email);
        return sendMail(email, false);
    }

    public ResponseEntity<Object> sendMail(String email , boolean isForPasswordReset) throws InterruptedException {
        int otp = (int) (Math.random() * 1000000);
        OTP otpDto = new OTP(email , String.valueOf(otp), String.valueOf(new Date(System.currentTimeMillis()).getTime() + 1000 * 60 * 10));
        oTPValidationRepository.save(otpDto);
        HashMap<String, String> response = new HashMap<>();

        Thread emailThread = new Thread(() -> {
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setEmailTo(email);
            emailConfig.setOtp(String.valueOf(otp));
            SimpleMailMessage message = isForPasswordReset ? emailConfig.emailTemplate() : emailConfig.verificationTemplate();
            mailSender.send(message);
            // Schedeule auto delete OTP after 10 mins
            deleteOTP.setOtp(otpDto);
            deleteOTP.run();
        });

        emailThread.start();
        emailThread.join(5000);

        response.put("message", "OTP Sent successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
