package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.constants.RestApiConstants;
import com.istiak.timezone.model.AuthorityConstants;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.model.UserData;
import com.istiak.timezone.model.UserResponse;
import com.istiak.timezone.service.UserService;
import com.istiak.timezone.service.UserValidationService;
import org.json.JSONObject;
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

@CrossOrigin(origins = "*", maxAge = 3600)
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

    @RequestMapping(value="signin",
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
    public ResponseEntity<UserData> getSingleUser(@PathVariable Long id){
        UserData userData = userService.getSingleUser(id);
        if(userData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userData);
    }

    @RequestMapping(value={"users","signup"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO){
        String errors = userValidationService.checkIfInvalidData(userDTO);
        if(errors.length()>0) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", errors);
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.BAD_REQUEST);
        }
        if(userValidationService.checkIfUserExists(userDTO.getUsername())) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", "User name already exists!");
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.CONFLICT);
        }
        userService.createUser(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="users",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<?> updateUser(@RequestBody UserData userData){
        if(userData == null || userData.getUsername()== null || userData.getUsername().length()==0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if(userData.getUsername().equalsIgnoreCase(authenticatedUser)) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", "Can't update own data!");
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.BAD_REQUEST);
        }
        
        HttpStatus httpStatus = userValidationService.checkIfUserExists(userData.getUsername())? null : HttpStatus.NOT_FOUND;
        if(httpStatus == null) {
            httpStatus = userService.updateUser(userData) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value="users/{username}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        if(username == null || username.length()==0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String authenticatedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if(username.equalsIgnoreCase(authenticatedUser)) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", "Can't delete own data!");
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.BAD_REQUEST);
        }
        HttpStatus httpStatus = userService.deleteUser(username)? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(httpStatus);
    }
}
