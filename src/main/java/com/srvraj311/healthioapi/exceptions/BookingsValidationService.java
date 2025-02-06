package com.srvraj311.healthioapi.exceptions;

import com.srvraj311.healthioapi.repository.Hospital.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingsValidationService {
    @Autowired
    private HospitalRepository hospitalRepository; // Assuming HospitalRepository is your repository for hospital data

    public void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new ControllerExceptions.BadRequestException(fieldName + " cannot be null");
        }
    }

    public void validateFieldNotEmpty(String fieldValue, String fieldName) {
        if (fieldValue.isEmpty()) {
            throw new ControllerExceptions.BadRequestException(fieldName + " cannot be empty");
        }
    }
}