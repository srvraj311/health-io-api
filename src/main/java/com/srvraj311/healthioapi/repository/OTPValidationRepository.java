/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.repository;

import com.srvraj311.healthioapi.models.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface OTPValidationRepository extends MongoRepository<OTP, String> {
    @Query("{'otp': ?0}") // can be used for more than one field
    Optional<OTP> findByOtp(String otp);

    @Query("{'email': ?0}") // can be used for more than one field
    Optional<OTP> findByEmail(String email);
}
