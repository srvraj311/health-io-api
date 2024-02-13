package com.srvraj311.healthioapi.utils;

import com.srvraj311.healthioapi.models.BaseModel;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class AppUtil {
    public static ResponseEntity<Object> getErrorObjectFromResponseEntity(ResponseEntity<String> entity) {
        HashMap<String, String> responseObj = new HashMap<>();
        responseObj.put(Constants.STATUS, String.valueOf(entity.getStatusCode()));
        responseObj.put(Constants.MESSAGE, String.valueOf(entity.getBody()));
        return ResponseEntity.status(entity.getStatusCode()).body(responseObj);
    }

    public static HashMap<String, String> getEmptyMap() {
        return new HashMap<String, String>();
    }

    public static HashMap<String, String> getEmptyMap(String message) {
        HashMap<String, String> response = new HashMap<>();
        response.put(Constants.MESSAGE, message);
        return response;
    }

    public static BaseModel markAsDeleted(BaseModel model) {
        model.setDeletedAt(LocalDateTime.now());
        return model;
    };
}
