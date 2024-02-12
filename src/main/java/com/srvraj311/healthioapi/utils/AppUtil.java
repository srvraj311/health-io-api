package com.srvraj311.healthioapi.utils;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class AppUtil {
    public static ResponseEntity<Object> getErrorObjectFromResponseEntity(ResponseEntity<String> entity) {
        HashMap<String, String> responseObj = new HashMap<>();
        responseObj.put(Constants.STATUS, String.valueOf(entity.getStatusCode()));
        responseObj.put(Constants.MESSAGE, String.valueOf(entity.getBody()));
        return ResponseEntity.status(entity.getStatusCode()).body(responseObj);
    }
}
