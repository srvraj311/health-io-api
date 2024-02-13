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
@Document("hospital")
public class Hospital extends BaseModel {
    @Id
    private String id;
    private String name;
    private String city;
    private String geolocation;
    private String type;
    private String last_updated;
    private String opening_time;
    private String closing_time;
}