package com.inc.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    private CafeUtils() {
    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(responseMessage);

    }
}
