package com.srvraj311.healthioapi.service;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ResponseMap;
import com.srvraj311.healthioapi.exceptions.BookingsValidationService;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.HospitalValidationService;
import com.srvraj311.healthioapi.exceptions.UserValidationService;
import com.srvraj311.healthioapi.models.Booking;
import com.srvraj311.healthioapi.models.Hospital.BloodBank;
import com.srvraj311.healthioapi.models.Hospital.Hospital;
import com.srvraj311.healthioapi.models.Hospital.HospitalAmenities;
import com.srvraj311.healthioapi.models.Hospital.HospitalAvailability;
import com.srvraj311.healthioapi.repository.BookingCustomRepositoryImpl;
import com.srvraj311.healthioapi.repository.BookingRepository;
import com.srvraj311.healthioapi.repository.Hospital.HospitalRepository;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private BookingCustomRepositoryImpl bookingCustomRepository;
    private HospitalValidationService hospitalValidationService;
    private BookingsValidationService bookingsValidationService;
    private HospitalRepository hospitalRepository;

    public ResponseEntity<ApiResponse> createBooking(Booking bookingRequest) {
        bookingsValidationService.validateNotNull(bookingRequest, "Booking Request");
        bookingsValidationService.validateNotNull(bookingRequest.getHospital_id(), "Hospital ID");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getHospital_id(), "Hospital ID");
        hospitalValidationService.validateHospitalExistsById(bookingRequest.getHospital_id());
        bookingsValidationService.validateNotNull(bookingRequest.getName(), "Name");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getName(), "Name");
        bookingsValidationService.validateNotNull(bookingRequest.getPhone(), "Phone");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getPhone(), "Phone");
        bookingsValidationService.validateNotNull(bookingRequest.getEmail(), "Email");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getEmail(), "Email");
        bookingsValidationService.validateNotNull(bookingRequest.getDate(), "Date");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getDate(), "Date");
        bookingsValidationService.validateNotNull(bookingRequest.getTime(), "Time");
        bookingsValidationService.validateFieldNotEmpty(bookingRequest.getTime(), "Time");
        bookingsValidationService.validateNotNull(bookingRequest.getAge(), "Age");
        bookingsValidationService.validateNotNull(bookingRequest.getTimestamp(), "Timestamp");
        bookingsValidationService.validateNotNull(bookingRequest.getStatus(), "Status");
        bookingsValidationService.validateNotNull(bookingRequest.getType(), "Type");

        Optional<Hospital> hospital = hospitalRepository.findByLicenceId(bookingRequest.getHospital_id());
        if (hospital.isPresent()) {
            bookingRequest.setHospital_name(hospital.get().getName());
        } else {
            throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
        }

        Booking booking = bookingRepository.save(bookingRequest);
        ResponseMap response = ResponseMap.builder().build();
        response.put("message", "Booking successfull");
        response.put("booking", booking);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(response)
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> getBookings(String hospitalId, String type, String status) {
        bookingsValidationService.validateNotNull(hospitalId, "Hospital ID");

        List<Booking> bookings = bookingCustomRepository.findBookingsByFilters(hospitalId, type, status);

        if (!bookings.isEmpty()) {
            ResponseMap response = ResponseMap.builder().build();
            response.put("message", "Bookings found");
            response.put("bookings", bookings);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(response)
                            .build()
            );
        } else {
            throw new ControllerExceptions.NotFoundException("No bookings found with the given filters.");
        }
    }

}
