/*
 * Copyright (c) 2021 Sourabh , The following code is a part of Project health.io ,
 * All rights reserved to Sourabh (Srvraj311)
 *
 */

package com.srvraj311.healthioapi.repository.Hospital;

import com.srvraj311.healthioapi.models.Hospital.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends MongoRepository<Hospital, String> {
    @Query("{'name':  ?0}")
    Optional<Hospital> findByName(String name);

    @Query("{'_id': ?0}") // can be used for more than one field
    Optional<Hospital> findByLicenceId(String id);

    @Query("{'city_name': ?0}")
    Optional<List<Hospital>> findAllByCity(String city);

    @Query("{'distinct' :  'city_name'}")
    Optional<List<String>> getDistricts();
}
