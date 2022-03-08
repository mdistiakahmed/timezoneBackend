package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

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

    @GetMapping("/all-test")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public String allTestMethod() {
        return "hello All";
    }

    @GetMapping("/token")
    public ResponseEntity<String> authenticateUser(@RequestBody UserDTO userDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(token);
    }
}
