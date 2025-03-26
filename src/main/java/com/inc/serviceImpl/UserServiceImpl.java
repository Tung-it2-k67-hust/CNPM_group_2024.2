package com.inc.serviceImpl;

import com.inc.JWT.CustomerUsersDetailsService;
import com.inc.JWT.JwtFilter;
import com.inc.JWT.JwtUtil;
import com.inc.POJO.User;
import com.inc.constants.CafeConstant;
import com.inc.dao.UserDao;
import com.inc.service.UserService;
import com.inc.utils.CafeUtils;
import com.inc.utils.EmailUtils;
import com.inc.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil; // Ensure jwtUtil is defined and injected

    @Autowired
    JwtFilter jwtFilter; // Ensure jwtFilter is defined and injected
    @Autowired
    EmailUtils emailUtils; // Ensure emailUtils is defined and injected
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup{}", requestMap);

        try {
            if (validateRequest(requestMap)) {
                User user = userDao.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("User signed up successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Already exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity("Error signing up user", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception occurred during signup", e);
            return CafeUtils.getResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            if (auth.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(), customerUsersDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("{\"message\":\"Wait for admin approval.\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("Exception occurred while login", ex);
        }
        return new ResponseEntity<>("{\"message\":\"Bad Credentials.\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {//check admin and return all user
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex){
            log.error("Exception occurred while fetching all users", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(Long.parseLong(requestMap.get("id")));
                if (optional.isPresent()) {
                    userDao.updateStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Exception occurred while updating user status", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }//check if this is admin or user

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(userObj != null){
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtils.getResponseEntity("Password Updated Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(Exception ex) {
            log.error("Exception occurred while changing password", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = userDao.findByEmail(requestMap.get("email"));
            if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
                emailUtils.forgotMail(user.getEmail(), "Credentials by Coffee Management System", user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check your mail for Credentials.", HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception occurred while using forgot password", ex);
        }
        return CafeUtils.getResponseEntity(CafeConstant.Something_Went_Wrong, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- "+user+" \n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:- "+user+" \n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
        }
    }

    private boolean validateRequest(Map<String, String> requestMap) {
        return requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}