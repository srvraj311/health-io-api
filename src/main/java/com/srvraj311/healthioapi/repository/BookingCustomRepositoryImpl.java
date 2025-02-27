package com.srvraj311.healthioapi.repository;

import com.srvraj311.healthioapi.models.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class BookingCustomRepositoryImpl implements BookingCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Booking> findBookingsByFilters(String hospitalId, String type, String status) {
        Criteria criteria = new Criteria();

        if (hospitalId != null && !hospitalId.isEmpty()) {
            criteria.and("hospital_id").regex(Pattern.compile(hospitalId, Pattern.CASE_INSENSITIVE));
        }
        if (type != null && !type.isEmpty()) {
            criteria.and("type").regex(Pattern.compile(type, Pattern.CASE_INSENSITIVE));
        }
        if (status != null && !status.isEmpty()) {
            criteria.and("status").regex(Pattern.compile(status, Pattern.CASE_INSENSITIVE));
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Booking.class);
    }
}
