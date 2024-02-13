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
@Document("hospital_info")
public class HospitalInfo extends BaseModel {
    @Id
    private String id;
    private String hospital_id;
    private String address;
    private String city_name;
    private String state_name;
    private String geolocation;
}