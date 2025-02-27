package com.srvraj311.healthioapi.controller;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ResponseMap;
import com.srvraj311.healthioapi.models.Booking;
import com.srvraj311.healthioapi.models.Hospital.Hospital;
import com.srvraj311.healthioapi.service.BookingService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

import static com.srvraj311.healthioapi.utils.Constants.OK;

@RestController
@RequestMapping("/api/v1/client/booking")
@AllArgsConstructor
@CrossOrigin("*")
public class BookingController {
    // TODO : Implement booking, Cancel and Reschedule etc everything.
    // TODO : Try to keep hospital and clinic seperate in design

    private BookingService bookingService;

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') || hasAuthority('ROLE_USER') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_DOCTOR') || hasAuthority('ROLE_USER') || hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getBookings(
            @RequestParam(value = "hospital_id") String hospitalId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status) {

        ResponseMap responseMap = new ResponseMap();
        return bookingService.getBookings(hospitalId, type, status);
    }
}
