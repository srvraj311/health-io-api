package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.HospitalAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface HospitalAvailabilityRepository extends MongoRepository<HospitalAvailability, String> {
    @Query("{'hospital_id' :  ?0}")
    Optional<HospitalAvailability> findOneByHospitalId(String hospital_id);
}
