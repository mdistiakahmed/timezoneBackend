package com.istiak.timezone.controller;

import com.istiak.timezone.config.JwtUtil;
import com.istiak.timezone.constants.RestApiConstants;
import com.istiak.timezone.model.*;
import com.istiak.timezone.service.UserService;
import com.istiak.timezone.service.UserValidationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private UserService  userService;

    @RequestMapping(value="users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<UserTableResponse> getAllUser(
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        UserTableResponse userTableResponse = userService.getAllUser(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(userTableResponse);
    }

    @RequestMapping(value="user",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody UserCreateModel userCreateModel){
        // return errors as json
        String errors = userValidationService.validate(userCreateModel);
        if(errors.length()>0) {
            return new ResponseEntity<String>(errors,HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userCreateModel);
        return new ResponseEntity<>("User Created",HttpStatus.CREATED);
    }

    @RequestMapping(value="user",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateModel userUpdateModel){
        if(userUpdateModel == null) {
            return new ResponseEntity<>("No data to update",HttpStatus.BAD_REQUEST);
        }
        String errors = userValidationService.userModifyValidate(userUpdateModel.getEmail());
        if(errors.length() > 0) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", errors);
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.BAD_REQUEST);
        }

        userService.updateUser(userUpdateModel);

        return new ResponseEntity<>("User Updated",HttpStatus.OK);
    }

    @RequestMapping(value="users/{username}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        String errors = userValidationService.userModifyValidate(username);
        if(errors.length() > 0) {
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("msg", errors);
            return new ResponseEntity<>(errorMsg.toString(),HttpStatus.BAD_REQUEST);
        }
        userService.deleteUser(username);
        return new ResponseEntity<>("User Deleted",HttpStatus.OK);
    }

}
