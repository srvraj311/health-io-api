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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class User extends BaseModel {
    @Id
    private String id;
    //@FieldName(first_name = "f_name") To change how this is saved in db
    private String first_name;
    private String last_name;
    private String mobile_num;
    @Indexed(unique = true) //means cannot store more than one common email address
    private String email;
    private String password;
    private String age;
    private String role;
    private String gender;
    private String dob;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String blood_group;
}
