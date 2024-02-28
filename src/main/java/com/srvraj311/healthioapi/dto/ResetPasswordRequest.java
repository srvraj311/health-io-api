package com.srvraj311.healthioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String password;
}
