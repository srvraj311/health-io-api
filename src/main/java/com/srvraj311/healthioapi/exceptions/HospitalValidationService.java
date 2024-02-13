package com.srvraj311.healthioapi.exceptions;

import com.srvraj311.healthioapi.repository.Hospital.HospitalInfoRepository;
import com.srvraj311.healthioapi.repository.Hospital.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalValidationService {
    @Autowired
    private HospitalRepository hospitalRepository; // Assuming HospitalRepository is your repository for hospital data
    @Autowired
    private HospitalInfoRepository hospitalInfoRepository;

    public void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new ControllerExceptions.BadRequestException(fieldName + " cannot be null");
        }
    }

    public void validateHospitalNotExistsByName(String name) {
        if (hospitalRepository.findByName(name).isPresent()) {
            throw new ControllerExceptions.BadRequestException("Hospital with name " + name + " already exists");
        }
    }

    public void validateFieldNotEmpty(String fieldValue, String fieldName) {
        if (fieldValue.isEmpty()) {
            throw new ControllerExceptions.BadRequestException(fieldName + " cannot be empty");
        }
    }

    public void validateHospitalNotExistsById(String id) {
        if (hospitalRepository.findByLicenceId(id).isPresent()) {
            throw new ControllerExceptions.DataAlreadyExistsException("Hospital with id : " + id + " already exists");
        }
    }

    public void validateHospitalExistsById(String id) {
        if (!hospitalRepository.findByLicenceId(id).isPresent()) {
            throw new ControllerExceptions.NotFoundException("Hospital with id : " + id + " is not present");
        }
    }

//    public void validateHospitalInfoNotExistsByHospitalId(String hospital_id){
//        if (hospitalInfoRepository.findOneByHospitalId(hospital_id).isPresent()) {
//            throw new ControllerExceptions.DataAlreadyExistsException("Hospital Info already exists for this" + )
//        }
//    }
}
