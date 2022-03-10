package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.constants.RestApiConstants;
import com.istiak.timezone.model.AuthorityConstants;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.model.UserResponse;
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

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserService  userService;

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


    @RequestMapping(value="users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<UserResponse> getAllUser(
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        UserResponse userResponse = userService.getAllUser(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(userResponse);
    }

    @RequestMapping(value="users/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<UserDTO> getSingleUser(@PathVariable Long id){
        UserDTO userDTO = userService.getSingleUser(id);
        if(userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping(value="users",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO){
        if(userValidationService.checkIfUserExists(userDTO)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.createUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="users",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO){
        HttpStatus httpStatus = userValidationService.checkIfUserExists(userDTO)? null : HttpStatus.NOT_FOUND;
        if(httpStatus == null) {
            httpStatus = userService.updateUser(userDTO) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value="users/{username}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<Void> deleteUser(@PathVariable String username){
        HttpStatus httpStatus = userService.deleteUser(username)? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(httpStatus);
    }
}
