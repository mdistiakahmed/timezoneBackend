package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.model.*;
import com.istiak.timezone.service.UserService;
import com.istiak.timezone.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthorityController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="sign-in",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> authenticateUser(@RequestBody UserSignInModel userSignInModel) {
        final String token = authenticateAndGenerateToken(userSignInModel.getEmail(), userSignInModel.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @RequestMapping(value="sign-up",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userSignUp(@RequestBody UserCreateModel userCreateModel){
        // return errors as json
        String errors = userValidationService.validate(userCreateModel);
        if(errors.length()>0) {
            return new ResponseEntity<String>(errors, HttpStatus.BAD_REQUEST);
        }

        User createdUser = userService.createUser(userCreateModel);
        if(createdUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = authenticateAndGenerateToken(userCreateModel.getEmail(), userCreateModel.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    private String authenticateAndGenerateToken(String username, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return token;
    }
}
