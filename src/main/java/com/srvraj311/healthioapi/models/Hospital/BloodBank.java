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
@Document("bloodbank")
public class BloodBank extends BaseModel {
    @Id
    private String id;
    private String hospital_id;
    private int o_negative;
    private int ab_negative;
    private int a_negative;
    private int b_positive;
    private int o_positive;
    private int b_negative;
    private int ab_positive;
    private int a_positive;
    private int other;
}