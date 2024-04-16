package com.srvraj311.healthioapi.controller;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ResponseMap;
import com.srvraj311.healthioapi.models.Hospital.Hospital;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.srvraj311.healthioapi.utils.Constants.OK;

@RestController
@RequestMapping("/api/v1/client/booking")
@AllArgsConstructor
public class BookingController {

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
