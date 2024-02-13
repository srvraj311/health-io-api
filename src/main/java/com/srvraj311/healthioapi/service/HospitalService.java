package com.srvraj311.healthioapi.service;

import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.HospitalValidationService;
import com.srvraj311.healthioapi.models.Hospital.*;
import com.srvraj311.healthioapi.repository.Hospital.*;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalAmenitiesRepository hospitalAmenitiesRepository;
    private final HospitalAvailabilityRepository hospitalAvailabilityRepository;
    private final HospitalValidationService hospitalValidationService;
    private final HospitalInfoRepository hospitalInfoRepository;
    private final BloodBankRepository bloodBankRepository;

    public HospitalService(HospitalRepository hospitalRepository,
                           HospitalAmenitiesRepository hospitalAmenitiesRepository,
                           HospitalAvailabilityRepository hospitalAvailabilityRepository, HospitalValidationService hospitalValidationService,
                           HospitalInfoRepository hospitalInfoRepository,
                           BloodBankRepository bloodBankRepository) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalAmenitiesRepository = hospitalAmenitiesRepository;
        this.hospitalAvailabilityRepository = hospitalAvailabilityRepository;
        this.hospitalValidationService = hospitalValidationService;
        this.hospitalInfoRepository = hospitalInfoRepository;
        this.bloodBankRepository = bloodBankRepository;
    }

    public ResponseEntity<Object> addHospital(Hospital hospitalReq) {
        hospitalValidationService.validateNotNull(hospitalReq, "Hospital");
        hospitalValidationService.validateNotNull(hospitalReq.getId(), "ID");
        hospitalValidationService.validateFieldNotEmpty(hospitalReq.getId(), "ID");
        hospitalValidationService.validateHospitalNotExistsById(hospitalReq.getId());
        hospitalValidationService.validateFieldNotEmpty(hospitalReq.getGeolocation(), "Geolocation");
        hospitalValidationService.validateFieldNotEmpty(hospitalReq.getCity(), "City");
        hospitalValidationService.validateFieldNotEmpty(hospitalReq.getType(), "Type");
        hospitalValidationService.validateFieldNotEmpty(hospitalReq.getName(), "Name");
        hospitalValidationService.validateHospitalNotExistsByName(hospitalReq.getName());

        Hospital hospital = hospitalRepository.insert(hospitalReq);
        String id = hospital.getId();

        HospitalInfo hospitalInfo = new HospitalInfo();
        hospitalInfo.setHospital_id(id);
        hospitalInfoRepository.save(hospitalInfo);

        HospitalAvailability availability = new HospitalAvailability();
        availability.setHospital_id(id);
        hospitalAvailabilityRepository.save(availability);

        HospitalAmenities amenities = new HospitalAmenities();
        amenities.setHospital_id(id);
        hospitalAmenitiesRepository.save(amenities);

        BloodBank bloodBank = new BloodBank();
        bloodBank.setHospital_id(id);
        bloodBankRepository.save(bloodBank);
        return ResponseEntity.ok().body(AppUtil.getEmptyMap("Hospital created successfully"));
    }

    public ResponseEntity<Object> updateHospitalData(HashMap<String, String> requestObj, String command) {
        return switch (command) {
            case Constants.CMD_UPDATE_HOSPITAL_INFO -> updateHospitalInfo(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_AMENITIES -> updateHospitalAmenities(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_AVAILABILITY -> updateHospitalAvailability(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_BLOOD_BANK -> updateHospitalBloodBank(requestObj);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    private ResponseEntity<Object> updateHospitalAmenities(HashMap<String, String> requestObj) {
        HospitalAmenities amenities = new ModelMapper().map(requestObj, HospitalAmenities.class);
        hospitalValidationService.validateNotNull(amenities, "Hospital Amenities");
        hospitalValidationService.validateFieldNotEmpty(amenities.getHospital_id(), "Hospital Id");
        Optional<HospitalAmenities> dbAmenites = hospitalAmenitiesRepository.findOneByHospitalId(amenities.getHospital_id());
        if (dbAmenites.isPresent()) {
            amenities.setId(dbAmenites.get().getId());
            hospitalAmenitiesRepository.save(amenities);
            return ResponseEntity.ok(AppUtil.getEmptyMap("Hospital Amenities updated successfully"));
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }

    private ResponseEntity<Object> updateHospitalInfo(HashMap<String, String> requestObj) {
        HospitalInfo hospitalInfo = new ModelMapper().map(requestObj, HospitalInfo.class);
        hospitalValidationService.validateNotNull(hospitalInfo, "Hospital Info");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getHospital_id(), "Hospital Id");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getCity_name(), "City Name");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getGeolocation(), "Hospital Geolocation");
        Optional<HospitalInfo> info = hospitalInfoRepository.findOneByHospitalId(hospitalInfo.getHospital_id());
        if (info.isPresent()) {
            hospitalInfo.setId(info.get().getId());
            hospitalInfoRepository.save(hospitalInfo);
            return ResponseEntity.ok(AppUtil.getEmptyMap("Hospital info updated successfully"));
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }
    private ResponseEntity<Object> updateHospitalAvailability(HashMap<String, String> requestObj) {
        HospitalAvailability availability = new ModelMapper().map(requestObj, HospitalAvailability.class);
        hospitalValidationService.validateNotNull(availability, "Hospital Availability");
        hospitalValidationService.validateFieldNotEmpty(availability.getHospital_id(), "Hospital Id");
        Optional<HospitalAvailability> dbAvailability = hospitalAvailabilityRepository.findOneByHospitalId(availability.getHospital_id());
        if (dbAvailability.isPresent()) {
            availability.setId(dbAvailability.get().getId());
            hospitalAvailabilityRepository.save(availability);
            return ResponseEntity.ok(AppUtil.getEmptyMap("Hospital Availability updated successfully"));
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }
    private ResponseEntity<Object> updateHospitalBloodBank(HashMap<String, String> requestObj) {
        BloodBank bloodBank = new ModelMapper().map(requestObj, BloodBank.class);
        hospitalValidationService.validateNotNull(bloodBank, "Hospital Blood Bank");
        hospitalValidationService.validateFieldNotEmpty(bloodBank.getHospital_id(), "Hospital Id");
        Optional<BloodBank> dbBloodBank = bloodBankRepository.findOneByHospitalId(bloodBank.getHospital_id());
        if (dbBloodBank.isPresent()) {
            bloodBank.setId(dbBloodBank.get().getId());
            bloodBankRepository.save(bloodBank);
            return ResponseEntity.ok(AppUtil.getEmptyMap("Hospital Blood Bank updated successfully"));
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }


    public ResponseEntity<Object> deleteHospital(String hospitalId) {
        hospitalValidationService.validateNotNull(hospitalId, "Hospital ID");
        hospitalValidationService.validateFieldNotEmpty(hospitalId, "Hospital ID");
        hospitalValidationService.validateHospitalExistsById(hospitalId);

        Optional<Hospital> hospital = hospitalRepository.findByLicenceId(hospitalId);
        if(hospital.isPresent()){
            hospital.get().setDeletedAt(LocalDateTime.now());
            hospitalRepository.save(hospital.get());
        }

        Optional<HospitalAmenities> amenities  = hospitalAmenitiesRepository.findOneByHospitalId(hospitalId);
        if(amenities.isPresent()){
            amenities.get().setDeletedAt(LocalDateTime.now());
            hospitalAmenitiesRepository.save(amenities.get());
        }
        Optional<HospitalAvailability> availability = hospitalAvailabilityRepository.findOneByHospitalId(hospitalId);
        if(availability.isPresent()){
            availability.get().setDeletedAt(LocalDateTime.now());
            hospitalAvailabilityRepository.save(availability.get());
        }
        Optional<HospitalInfo> hospitalInfo = hospitalInfoRepository.findOneByHospitalId(hospitalId);
        if(hospitalInfo.isPresent()){
            hospitalInfo.get().setDeletedAt(LocalDateTime.now());
            hospitalInfoRepository.save(hospitalInfo.get());
        }
        Optional<BloodBank> bloodBank = bloodBankRepository.findOneByHospitalId(hospitalId);
        if(bloodBank.isPresent()){
            bloodBank.get().setDeletedAt(LocalDateTime.now());
            bloodBankRepository.save(bloodBank.get());
        }

        return ResponseEntity.ok(AppUtil.getEmptyMap("Hospital Deleted Successfully"));
    }
}
