package com.srvraj311.healthioapi.dto;

import com.srvraj311.healthioapi.models.User;

public class SignupRequestWithOtp extends User {
    private String otp;

    public SignupRequestWithOtp(String id, String first_name, String last_name, String mobile_num, String email, String password, String age, String role, String gender, String dob, String address, String city, String state, String country, String pincode, String blood_group, String otp) {
        super(id, first_name, last_name, mobile_num, email, password, age, role, gender, dob, address, city, state, country, pincode, blood_group);
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
