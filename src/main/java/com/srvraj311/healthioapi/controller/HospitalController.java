package com.srvraj311.healthioapi.controller;

import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.models.Hospital.Hospital;
import com.srvraj311.healthioapi.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/hospital")
@AllArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN , ROLE_DATA_FEEDER')")
    public ResponseEntity<ApiResponse> addHospital(@RequestBody Hospital hospital) {
        return hospitalService.addHospital(hospital);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN, ROLE_DATA_FEEDER')")
    public ResponseEntity<ApiResponse> updateHospitalDetails(@RequestBody HashMap<String, String> requestObj, @Param("command") String command){
        return hospitalService.updateHospitalData(requestObj, command);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteHospital(@Param("hospital_id") String hospital_id) {
        return hospitalService.deleteHospital(hospital_id);
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse> getHospital(@Param("hospital_id") String hospital_id) {
        return hospitalService.getHospital(hospital_id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse> getAllhospital() {
        return hospitalService.getAllHospital();
    }
}
