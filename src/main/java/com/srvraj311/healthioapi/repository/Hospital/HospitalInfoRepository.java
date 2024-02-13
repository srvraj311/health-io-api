package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.HospitalInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface HospitalInfoRepository extends MongoRepository<HospitalInfo, String> {
    @Query("{'hospital_id' :  ?0}")
    Optional<HospitalInfo> findOneByHospitalId(String hospital_id);
}
