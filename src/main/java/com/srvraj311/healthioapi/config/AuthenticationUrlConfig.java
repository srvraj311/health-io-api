package com.srvraj311.healthioapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUrlConfig {
    public static final String AUTH_V1_URL = "/api/v1/auth";
    public static final String API_V1_URL = "/api/v1";
    public static final String SIGNUP_URL = "/signup";    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String FORGOT_PASSWORD_URL = "/forgot-password";
    public static final String RESET_PASSWORD_URL = "/reset-password";
    public static final String CHANGE_PASSWORD_URL = "/change-password";
    public static final String CHANGE_EMAIL_URL = "/change-email";
    public static final String CHANGE_USERNAME_URL = "/change-username";
    public static final String SEND_OTP = "/send_otp";
    public static final String TEST_URL = "/users";
    public static final String CLIENT_URL = "/client";
    public static final String SUB_URLS = "/*";
    public static final String VALIDATE_USER = "/validate";
    public static final String AUTHENTICATE_URL = "/authenticate";
    public static final String HOSTPITAL_URL = "/hospital";
    public static final String VERIFY_OTP = "/verify_otp";


    @Bean
    public String[] getAllowedEndpoints() {
        return new String[]{
                AUTH_V1_URL+ SIGNUP_URL,
                AUTH_V1_URL+ LOGIN_URL,
                AUTH_V1_URL+ LOGOUT_URL,
                AUTH_V1_URL+ FORGOT_PASSWORD_URL,
                AUTH_V1_URL+ RESET_PASSWORD_URL,
                AUTH_V1_URL+ CHANGE_PASSWORD_URL,
                AUTH_V1_URL+ CHANGE_EMAIL_URL,
                AUTH_V1_URL+ CHANGE_USERNAME_URL,
                AUTH_V1_URL+ AUTHENTICATE_URL,
                AUTH_V1_URL+ SEND_OTP,
                AUTH_V1_URL+ VALIDATE_USER,
                AUTH_V1_URL+ VERIFY_OTP,
        };
    }

    @Bean
    public String[] getAuthenticatedEndpoints() {
        return new String[]{
                AUTH_V1_URL+ TEST_URL,
                API_V1_URL + CLIENT_URL + SUB_URLS,
                API_V1_URL + HOSTPITAL_URL + SUB_URLS
        };
    }
}
