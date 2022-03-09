package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.model.AuthorityConstants;
import com.istiak.timezone.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public String testMethod() {
        return "hello world";
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/admin-test")
    public String adminTestMethod() {
        return "hello Admin";
    }

    @RolesAllowed({"ROLE_USER"})
    @GetMapping("/user-test")
    public String userTestMethod() {
        return "hello User";
    }

    @RequestMapping(value="all-test",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public String allTestMethod() {
        return "hello All";
    }

    @RequestMapping(value="token",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> authenticateUser(@RequestBody UserDTO userDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
