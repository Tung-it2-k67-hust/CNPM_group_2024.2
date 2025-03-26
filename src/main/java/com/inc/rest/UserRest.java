package com.inc.rest;

import com.inc.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRest {
    @PostMapping("/signup")
    public ResponseEntity<String>signUp(@RequestBody(required = true)Map<String, String> requestMap);
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);
    @GetMapping(path = "/getUser")
    public ResponseEntity<List<UserWrapper>> getAllUser();//get user by admin so that admin can see all user
    @PostMapping(path = "/update")//update user by admin
    public ResponseEntity<String> updateUser(@RequestBody(required = true) Map<String, String> requestMap);
    @GetMapping(path = "/checkToken")//verify user's token
    public ResponseEntity<String> checkToken();
    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
}
