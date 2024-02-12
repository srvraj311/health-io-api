/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {
    private String emailTo;
    private String otp;

    @Bean
    public SimpleMailMessage emailTemplate(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setFrom("health.io@yahoo.com");
        message.setSubject("!Important - Recover Password - Health.io");
        message.setText("Someone is trying to change password of account associated with " + emailTo + ". " +
                "If this is not you, Ignore" +
                "OTP for your password recovery is \n" + otp + " \t  \n valid for 10 minutes Only");
        return message;
    }
    @Bean
    public SimpleMailMessage verificationTemplate(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setFrom("health.io@yahoo.com");
        message.setSubject("!Important - Verify Your Email for Health-IO");
        message.setText("Thank You for registering for Health-IO, You are one step away from experiencing Health-IO,\n" +
                "Verify your email " + emailTo + " by verifyng the OTP " +
                "If this is not you, Ignore this mail. " +
                "OTP for your password recovery is \n \t" + otp + " \t  \n valid for 10 minutes Only");
        return message;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
