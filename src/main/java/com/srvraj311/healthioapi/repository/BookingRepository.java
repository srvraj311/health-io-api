package com.srvraj311.healthioapi.repository;

import com.srvraj311.healthioapi.models.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    @Query("{ email: ?0}")
        // can be used for more than one field
    Optional<Booking> findByEmail(String email);

    @Query("{ type: ?0}")
        // can be used for more than one field
    Optional<Booking> findByType(String type);

    @Query("{ status: ?0}")
        // can be used for more than one field
    Optional<Booking> findByStatus(String status);

    @Query("{ hospital_id: ?0}")
        // can be used for more than one field
    Optional<Booking[]> findByHospitalId(String hospital_id);


}