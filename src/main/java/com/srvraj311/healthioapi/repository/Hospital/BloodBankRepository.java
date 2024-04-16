package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.BloodBank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BloodBankRepository extends MongoRepository<BloodBank, String> {
    @Query("{'hospital_id' :  ?0}")
    Optional<BloodBank> findOneByHospitalId(String hospital_id);
}
