package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.HospitalAmenities;
import com.srvraj311.healthioapi.models.Hospital.HospitalInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface HospitalAmenitiesRepository extends MongoRepository<HospitalAmenities, String> {
    @Query("{'hospital_id' :  ?0}")
    Optional<HospitalAmenities> findOneByHospitalId(String hospital_id);
}
