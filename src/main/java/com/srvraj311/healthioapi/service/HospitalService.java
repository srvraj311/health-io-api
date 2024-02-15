package com.srvraj311.healthioapi.service;


import com.mongodb.client.MongoCursor;
import com.srvraj311.healthioapi.dto.ApiResponse;
import com.srvraj311.healthioapi.dto.ResponseMap;
import com.srvraj311.healthioapi.exceptions.ControllerExceptions;
import com.srvraj311.healthioapi.exceptions.HospitalValidationService;
import com.srvraj311.healthioapi.models.Hospital.*;
import com.srvraj311.healthioapi.repository.Hospital.*;
import com.srvraj311.healthioapi.utils.AppUtil;
import com.srvraj311.healthioapi.utils.Constants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalAmenitiesRepository hospitalAmenitiesRepository;
    private final HospitalAvailabilityRepository hospitalAvailabilityRepository;
    private final HospitalValidationService hospitalValidationService;
    private final HospitalInfoRepository hospitalInfoRepository;
    private final BloodBankRepository bloodBankRepository;
    private final MongoTemplate mongoTemplate;

    public ResponseEntity<ApiResponse> addHospital(Hospital hospitalReq) {
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
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(AppUtil.getEmptyMap("Hospital created successfully"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> updateHospitalData(HashMap<String, String> requestObj, String command) {
        return switch (command) {
            case Constants.CMD_UPDATE_HOSPITAL_INFO -> updateHospitalInfo(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_AMENITIES -> updateHospitalAmenities(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_AVAILABILITY -> updateHospitalAvailability(requestObj);
            case Constants.CMD_UPDATE_HOSPITAL_BLOOD_BANK -> updateHospitalBloodBank(requestObj);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    private ResponseEntity<ApiResponse> updateHospitalAmenities(HashMap<String, String> requestObj) {
        HospitalAmenities amenities = new ModelMapper().map(requestObj, HospitalAmenities.class);
        hospitalValidationService.validateNotNull(amenities, "Hospital Amenities");
        hospitalValidationService.validateFieldNotEmpty(amenities.getHospital_id(), "Hospital Id");
        Optional<HospitalAmenities> dbAmenites = hospitalAmenitiesRepository.findOneByHospitalId(amenities.getHospital_id());
        if (dbAmenites.isPresent()) {
            amenities.setId(dbAmenites.get().getId());
            hospitalAmenitiesRepository.save(amenities);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(AppUtil.getEmptyMap("Hospital Amenities updated successfully"))
                            .build()
            );
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }

    private ResponseEntity<ApiResponse> updateHospitalInfo(HashMap<String, String> requestObj) {
        HospitalInfo hospitalInfo = new ModelMapper().map(requestObj, HospitalInfo.class);
        hospitalValidationService.validateNotNull(hospitalInfo, "Hospital Info");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getHospital_id(), "Hospital Id");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getCity_name(), "City Name");
        hospitalValidationService.validateFieldNotEmpty(hospitalInfo.getGeolocation(), "Hospital Geolocation");
        Optional<HospitalInfo> info = hospitalInfoRepository.findOneByHospitalId(hospitalInfo.getHospital_id());
        if (info.isPresent()) {
            hospitalInfo.setId(info.get().getId());
            hospitalInfoRepository.save(hospitalInfo);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(AppUtil.getEmptyMap("Hospital Info updated successfully"))
                            .build()
            );
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }

    private ResponseEntity<ApiResponse> updateHospitalAvailability(HashMap<String, String> requestObj) {
        HospitalAvailability availability = new ModelMapper().map(requestObj, HospitalAvailability.class);
        hospitalValidationService.validateNotNull(availability, "Hospital Availability");
        hospitalValidationService.validateFieldNotEmpty(availability.getHospital_id(), "Hospital Id");
        Optional<HospitalAvailability> dbAvailability = hospitalAvailabilityRepository.findOneByHospitalId(availability.getHospital_id());
        if (dbAvailability.isPresent()) {
            availability.setId(dbAvailability.get().getId());
            hospitalAvailabilityRepository.save(availability);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(AppUtil.getEmptyMap("Hospital Availability updated successfully"))
                            .build()
            );
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }

    private ResponseEntity<ApiResponse> updateHospitalBloodBank(HashMap<String, String> requestObj) {
        BloodBank bloodBank = new ModelMapper().map(requestObj, BloodBank.class);
        hospitalValidationService.validateNotNull(bloodBank, "Hospital Blood Bank");
        hospitalValidationService.validateFieldNotEmpty(bloodBank.getHospital_id(), "Hospital Id");
        Optional<BloodBank> dbBloodBank = bloodBankRepository.findOneByHospitalId(bloodBank.getHospital_id());
        if (dbBloodBank.isPresent()) {
            bloodBank.setId(dbBloodBank.get().getId());
            bloodBankRepository.save(bloodBank);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(AppUtil.getEmptyMap("Hospital Blood Bank updated successfully"))
                            .build()
            );
        }

        throw new ControllerExceptions.NotFoundException("The hospital ID you provided is not present");
    }


    public ResponseEntity<ApiResponse> deleteHospital(String hospitalId) {
        hospitalValidationService.validateNotNull(hospitalId, "Hospital ID");
        hospitalValidationService.validateFieldNotEmpty(hospitalId, "Hospital ID");
        hospitalValidationService.validateHospitalExistsById(hospitalId);

        Optional<Hospital> hospital = hospitalRepository.findByLicenceId(hospitalId);
        if (hospital.isPresent()) {
            hospital.get().setDeletedAt(Instant.now());
            hospitalRepository.save(hospital.get());
        }

        Optional<HospitalAmenities> amenities = hospitalAmenitiesRepository.findOneByHospitalId(hospitalId);
        if (amenities.isPresent()) {
            amenities.get().setDeletedAt(Instant.now());
            hospitalAmenitiesRepository.save(amenities.get());
        }
        Optional<HospitalAvailability> availability = hospitalAvailabilityRepository.findOneByHospitalId(hospitalId);
        if (availability.isPresent()) {
            availability.get().setDeletedAt(Instant.now());
            hospitalAvailabilityRepository.save(availability.get());
        }
        Optional<HospitalInfo> hospitalInfo = hospitalInfoRepository.findOneByHospitalId(hospitalId);
        if (hospitalInfo.isPresent()) {
            hospitalInfo.get().setDeletedAt(Instant.now());
            hospitalInfoRepository.save(hospitalInfo.get());
        }
        Optional<BloodBank> bloodBank = bloodBankRepository.findOneByHospitalId(hospitalId);
        if (bloodBank.isPresent()) {
            bloodBank.get().setDeletedAt(Instant.now());
            bloodBankRepository.save(bloodBank.get());
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(AppUtil.getEmptyMap("Hospital details deleted successfully"))
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> getHospital(String id) {
        hospitalValidationService.validateNotNull(id, "Hospital ID");
        hospitalValidationService.validateFieldNotEmpty(id, "Hospital ID");
        hospitalValidationService.validateHospitalExistsById(id);
        ResponseMap response = getHospitalDetailsById(id);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(response)
                        .build()
        );
    }

    public ResponseMap getHospitalDetailsById(String id) {
        ResponseMap response = ResponseMap.builder().build();

        Optional<Hospital> hospital = hospitalRepository.findByLicenceId(id);
        hospital.ifPresent(value -> response.put("hospital", value));

        Optional<HospitalAmenities> amenities = hospitalAmenitiesRepository.findOneByHospitalId(id);
        amenities.ifPresent(value -> response.put("amenities", value));

        Optional<HospitalAvailability> availability = hospitalAvailabilityRepository.findOneByHospitalId(id);
        availability.ifPresent(value -> response.put("availability", value));

        Optional<HospitalInfo> hospitalInfo = hospitalInfoRepository.findOneByHospitalId(id);
        hospitalInfo.ifPresent(value -> response.put("hospitalInfo", value));

        Optional<BloodBank> bloodBank = bloodBankRepository.findOneByHospitalId(id);
        bloodBank.ifPresent(value -> response.put("bloodBank", value));

        return response;
    }

    public ResponseEntity<ApiResponse> getAllHospital() {
        /*
            TODO : Pagination
            Page<Hospital> hospitalList = hospitalRepository.findAll(PageRequest.of(0, 10));
            List<Hospital> hospitals = hospitalList.get().collect(Collectors.toList());
        */
        List<Hospital> hospitalList = hospitalRepository.findAll();
        List<ResponseMap> detailsList = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            detailsList.add(this.getHospitalDetailsById(hospital.getId()));
        }

        ResponseMap map = ResponseMap.builder().build();
        map.put("hospitals", detailsList);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(map)
                        .build()
        );
    }

    public ResponseEntity<ApiResponse> getHospitalByCity(String cityName) {
        hospitalValidationService.validateNotNull(cityName, "City Name");
        hospitalValidationService.validateFieldNotEmpty(cityName, "City Name");
        Optional<List<Hospital>> hospitalList = hospitalRepository.findAllByCity(cityName.toLowerCase());
        if (hospitalList.isPresent()) {
            List<ResponseMap> detailsList = new ArrayList<>();
            for (Hospital hospital : hospitalList.get()) {
                detailsList.add(this.getHospitalDetailsById(hospital.getId()));
            }

            ResponseMap map = ResponseMap.builder().build();
            map.put("city", cityName);
            map.put("hospitals", detailsList);

            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .status(Constants.OK)
                            .body(map)
                            .build()
            );
        }

        throw new ControllerExceptions.NotFoundException("Hospitals not found for this city");
    }

    public ResponseEntity<ApiResponse> getAllHosiptalCity() {
        Query query = new Query();
        query.fields().include("city");
        MongoCursor<String> cities = mongoTemplate.getCollection("hospital").distinct("city", String.class).iterator();
        List<String> cityList = new ArrayList<>();
        while (cities.hasNext()) {
            cityList.add(cities.next());
        }

        ResponseMap responseMap = ResponseMap.builder().build();
        responseMap.put("cities", cityList);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .status(Constants.OK)
                        .body(responseMap)
                        .build()
        );
    }
}
