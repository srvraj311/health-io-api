/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.models.Hospital;

import com.srvraj311.healthioapi.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("emergency")
public class EmergencyCases extends BaseModel{
    @Id
    private String id;
    private String licence_id;
    private String name_of_patient;
    private String age;
    private String type_of_emergency;
    private String address;
    private String intensity_of_emergency;
    private String requirements;
    private String time;
    private String date;
    private String description;
}
