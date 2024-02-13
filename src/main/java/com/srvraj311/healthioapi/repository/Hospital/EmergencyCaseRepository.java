package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.EmergencyCases;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmergencyCaseRepository extends MongoRepository<EmergencyCases, String> {

}
