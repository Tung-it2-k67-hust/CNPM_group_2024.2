package com.inc.restImpl;

import com.inc.constants.CafeConstant;
import com.inc.rest.UserRest;
import com.inc.service.UserService;
import com.inc.utils.CafeUtils;
import com.inc.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class UserRestImpl implements UserRest {
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            log.error("Exception in signUp", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception ex) {
            log.error("Exception in login", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex) {
            log.error("Exception in getAllUser", ex);
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);
        } catch (Exception ex) {
            log.error("Exception in update", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception ex) {
            log.error("Exception in checkToken", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);
        } catch (Exception ex) {
            log.error("Exception in changePassword", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            return userService.forgotPassword(requestMap);
        } catch (Exception ex) {
            log.error("Exception in forgotPassword", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
