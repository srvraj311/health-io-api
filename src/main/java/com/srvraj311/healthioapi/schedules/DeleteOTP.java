/*******************************************************************************
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.schedules;

import com.srvraj311.healthioapi.models.OTP;
import com.srvraj311.healthioapi.repository.OTPValidationRepository;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
public class DeleteOTP {
    // This class will be initiated when creating a OTP for any purpose, its constructor will have email or otp
    // passed to its instace when getting created, Here in this class whenever this class is called , we will shcedule a
    // task to delete that particular otp after 10 minutes or so.

    private OTP otp;
    OTPValidationRepository otpValidationRepository;

    public DeleteOTP(OTPValidationRepository otpValidationRepository) {
        this.otpValidationRepository = otpValidationRepository;
    }

    public void setOtp(OTP otp) {
        this.otp = otp;
    }

    public void run(){
        // Create a scheduler
        TimerTask now = new TimerTask() {
            @Override
            public void run() {
                if(otpValidationRepository.findByOtp(otp.getOtp()).isPresent()){
                    otpValidationRepository.delete(otpValidationRepository.findByOtp(otp.getOtp()).orElseThrow(()-> new RuntimeException("OTP does not exists")));
                }
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 10);
                    now.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
