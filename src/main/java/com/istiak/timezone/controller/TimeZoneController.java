package com.istiak.timezone.controller;

import com.istiak.timezone.constants.RestApiConstants;
import com.istiak.timezone.model.AuthorityConstants;
import com.istiak.timezone.model.Timezone;
import com.istiak.timezone.model.TimezoneResponse;
import com.istiak.timezone.model.UserDTO;
import com.istiak.timezone.service.TimezoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
public class TimeZoneController {

    @Autowired
    private TimezoneService timezoneService;

    @RequestMapping(value="timezones",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<TimezoneResponse> getAllTimeZones(
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        TimezoneResponse timezoneResponse = timezoneService.getAllTimeZone(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(timezoneResponse);
    }

    @RequestMapping(value="timezones/users/{userid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<TimezoneResponse> getUserTimezone(
            @PathVariable Long userid,
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        TimezoneResponse timezoneResponse = timezoneService.getUserTimeZone(userid,pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(timezoneResponse);
    }

    @RequestMapping(value="timezones",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<Void> createTimezone(@RequestBody Timezone timezone){
        //TODO: validation or exception handling for same timezone name
        timezoneService.saveTimezone(timezone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="timezones",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<Void> updateTimezone(@RequestBody Timezone timezone){
        //TODO: validation for one user updating another user info
        timezoneService.saveTimezone(timezone);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="timezones/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<Void> deleteTimezone(@PathVariable Long id){
        //TODO: validation for one user deleting another user info
        timezoneService.deleteTimezoneById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
