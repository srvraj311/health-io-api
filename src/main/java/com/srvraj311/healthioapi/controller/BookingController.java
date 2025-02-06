package com.srvraj311.healthioapi.controller;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ResponseMap;
import com.srvraj311.healthioapi.models.Booking;
import com.srvraj311.healthioapi.models.Hospital.Hospital;
import com.srvraj311.healthioapi.service.BookingService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<ApiResponse> getBooking() {
        ResponseMap responseMap = new ResponseMap();
        responseMap.put("hospitalInfo", new Hospital());
        ApiResponse apiResponse = ApiResponse
                .builder()
                .status(OK)
                .body(responseMap)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
