package com.srvraj311.healthioapi.controller;

import com.srvraj311.healthioapi.models.Hospital.Hospital;
import com.srvraj311.healthioapi.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalController {

    @Autowired
    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DATA_FEEDER')")
    public ResponseEntity<Object> addHospital(@RequestBody Hospital hospital) {
        return hospitalService.addHospital(hospital);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DATA_FEEDER')")
    public ResponseEntity<Object> updateHospitalDetails(@RequestBody HashMap<String, String> requestObj, @Param("command") String command){
        return hospitalService.updateHospitalData(requestObj, command);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteHospital(@Param("hospital_id") String hospital_id) {
        return hospitalService.deleteHospital(hospital_id);
    }
}
