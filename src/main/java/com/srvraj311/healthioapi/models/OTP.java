/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("otp")
public class OTP extends BaseModel {
    @Id
    private String email;
    private String otp;
    private String expires_at;
}
