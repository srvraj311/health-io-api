package com.srvraj311.healthioapi.repository;

import com.srvraj311.healthioapi.models.Booking;

import java.util.List;

public interface BookingCustomRepository {
    List<Booking> findBookingsByFilters(String hospitalId, String type, String status);
}
