package com.istiak.timezone.service;

import com.istiak.timezone.model.TimeZoneDataModel;
import com.istiak.timezone.model.Timezone;
import com.istiak.timezone.repository.TimezoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TimeZoneValidationService {
    @Autowired
    private TimezoneRepository timezoneRepository;

    public String validateForCreate(TimeZoneDataModel timeZoneDataModel) {
        String errors = checkIfValidTimeZone(timeZoneDataModel);
        if(errors.length()>0) {
            return errors;
        }
        else if(checkIfTimeZoneAlreadyExists(timeZoneDataModel)) {
            return "TimeZone with same name already present. Use different name";
        }
        else return "";
    }

    public String validateForDelete(String name) {
        StringBuffer errorMsg = new StringBuffer("");
        if(name == null || name.trim().isEmpty()) {
            errorMsg.append("TimeZone name is empty, nothing to delete");
        } else {
            Timezone timezone =  timezoneRepository.findByName(name);
            if(timezone == null) {
                errorMsg.append("No timezone with this name exists. Aborting delete");
            }
        }

        return errorMsg.toString();
    }

    public String validateForUpdate(TimeZoneDataModel timeZoneDataModel) {
        if(timeZoneDataModel == null || timeZoneDataModel.getName().trim().isEmpty()) {
            return "Invalid data, nothing to update";
        }

        Timezone timezone = timezoneRepository.findByName(timeZoneDataModel.getName());
        if(timezone == null) {
            return "This timezone doesn't exist";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) authentication.getPrincipal();
        if(timezone.getEmail() != userEmail && !checkIfUserHasAdminRole()) {
            return "User doesn't have permission to delete";
        }

        return "";
    }

    public String checkIfValidTimeZone(TimeZoneDataModel timeZoneDataModel) {
        StringBuffer errorMsg = new StringBuffer("");
        if(timeZoneDataModel == null) {
            return "Not a valid entry";
        }
        if(timeZoneDataModel.getName().trim().isEmpty()) {
            errorMsg.append("Name can not be empty or only spaces");
        }
        if(timeZoneDataModel.getCity().trim().isEmpty()) {
            errorMsg.append("City can not be empty or only spaces");
        }

        if(timeZoneDataModel.getHourDiff()==null || timeZoneDataModel.getHourDiff()<-14 || timeZoneDataModel.getHourDiff()>12) {
            errorMsg.append("Hour Difference should be between -14 to 12");
        }

        if(timeZoneDataModel.getMinuteDiff()==null || timeZoneDataModel.getMinuteDiff()<0 || timeZoneDataModel.getMinuteDiff()>59) {
            errorMsg.append("Minute Difference should be between 0 to 59");
        }

        return errorMsg.toString();

    }

    public Boolean checkIfTimeZoneAlreadyExists(TimeZoneDataModel timeZoneDataModel) {
        Timezone timezone =  timezoneRepository.findByName(timeZoneDataModel.getName());

        return timezone != null;
    }

    private Boolean checkIfUserHasAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Optional<? extends GrantedAuthority> authority = authorities.stream()
                .filter(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"))
                .findFirst();
        if(authority.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
