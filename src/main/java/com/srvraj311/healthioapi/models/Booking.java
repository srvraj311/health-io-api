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
@Document("booking")
public class Booking extends BaseModel{

    @Id
    private String id;

    private String hospital_id;
    private String date;
    private String time;
    private String name;
    private String age;
    private String phone;
    private String number;
    private String timestamp;
    private String status;
    private String email;
    private String hospital_name;
    private String type;
}
