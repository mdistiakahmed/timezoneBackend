package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.model.User;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.model.UserSignUpModel;
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

    @RequestMapping(value="signin",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> authenticateUser(@RequestBody UserDTO userDTO) {
        final String token = authenticateAndGenerateToken(userDTO);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @RequestMapping(value="signup",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpModel userSignUpModel){
        UserDTO userDTO = UserDTO.of(userSignUpModel);
        // return errors as json
        String errors = userValidationService.validate(userDTO);
        if(errors.length()>0) {
            return new ResponseEntity<String>(errors, HttpStatus.BAD_REQUEST);
        }

        User createdUser = userService.createUser(userDTO);
        if(createdUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = authenticateAndGenerateToken(userDTO);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    private String authenticateAndGenerateToken(UserDTO userDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return token;
    }
}
