package com.istiak.timezone.controller;

import com.istiak.timezone.constants.RestApiConstants;
import com.istiak.timezone.model.AuthorityConstants;
import com.istiak.timezone.model.TimeZoneDataModel;
import com.istiak.timezone.model.Timezone;
import com.istiak.timezone.model.TimezoneTableResponseModel;
import com.istiak.timezone.service.TimeZoneValidationService;
import com.istiak.timezone.service.TimezoneService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class TimeZoneController {

    @Autowired
    private TimezoneService timezoneService;

    @Autowired
    private TimeZoneValidationService timeZoneValidationService;

    @RequestMapping(value="timezones",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<TimezoneTableResponseModel> getUserTimeZones(
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        TimezoneTableResponseModel timezoneResponse = timezoneService.getUserTimeZone(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(timezoneResponse);
    }

    @RequestMapping(value="timezones/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN})
    public ResponseEntity<TimezoneTableResponseModel> getAllUserTimezone(
            @RequestParam(value = "pageNo", defaultValue = RestApiConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestApiConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestApiConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        TimezoneTableResponseModel timezoneResponse = timezoneService.getAllUserTimeZone(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(timezoneResponse);
    }


    @RequestMapping(value="timezone",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<String> createTimezone(@RequestBody TimeZoneDataModel timeZoneDataModel){
        JSONObject response = new JSONObject();

        String errors = timeZoneValidationService.validateForCreate(timeZoneDataModel);
        if(errors.length() != 0) {
            response.put("msg", errors);
            return new ResponseEntity<>(response.toString(),HttpStatus.BAD_REQUEST);
        }
        Timezone timezone = TimeZoneDataModel.of(timeZoneDataModel);
        timezoneService.createTimezone(timezone);
        return new ResponseEntity<>(response.toString(), HttpStatus.CREATED);
    }


    @RequestMapping(value="timezone",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<String> updateTimezone(@RequestBody TimeZoneDataModel timeZoneDataModel){
        JSONObject response = new JSONObject();

        String errors = timeZoneValidationService.validateForUpdate(timeZoneDataModel);
        if(errors.length() != 0) {
            response.put("msg", errors);
            HttpStatus httpStatus = errors.contains("permission") ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response.toString(),httpStatus);
        }
        timezoneService.updateTimezone(timeZoneDataModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value="timezones/{name}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({AuthorityConstants.ADMIN, AuthorityConstants.USER})
    public ResponseEntity<String> deleteTimezone(@PathVariable String name){
        JSONObject response = new JSONObject();

        String errors = timeZoneValidationService.validateForDelete(name);
        if(errors.length() != 0) {
            response.put("msg", errors);
            return new ResponseEntity<>(response.toString(),HttpStatus.BAD_REQUEST);
        }
        timezoneService.deleteTimezoneByName(name);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
