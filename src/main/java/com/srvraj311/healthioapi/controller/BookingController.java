package com.srvraj311.healthioapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client/booking")
public class BookingController {

    @GetMapping()
    public String getBooking() {
        return "This is a booking";
    }
}
