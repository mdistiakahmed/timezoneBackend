package com.istiak.timezone.service;

import com.istiak.timezone.model.TimeZoneDataModel;
import com.istiak.timezone.model.Timezone;
import com.istiak.timezone.repository.TimezoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;

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
}
