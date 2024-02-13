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
@Document("hospital_availability")
public class HospitalAvailability extends BaseModel {
    @Id
    private String id;
    private String hospital_id;
    private int bed;
    private int total_bed;
    private int icu;
    private int total_icu;
    private int ccu;
    private int total_ccu;
    private int ventilator;
    private int total_ventilator;
    private int oxygen_cylinders;
    private int total_oxygen_cylinders;
}
