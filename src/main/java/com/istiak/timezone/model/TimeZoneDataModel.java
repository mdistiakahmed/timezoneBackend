package com.istiak.timezone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneDataModel {
    private String name;
    private String city;
    private Integer hourDiff;
    private Integer minuteDiff;

    public static Timezone of(TimeZoneDataModel timeZoneDataModel) {
        Timezone timezone = new Timezone();
        timezone.setName(timeZoneDataModel.getName());
        timezone.setCity(timeZoneDataModel.getCity());
        timezone.setHourdiff(timeZoneDataModel.getHourDiff());
        timezone.setMindiff(timeZoneDataModel.getMinuteDiff());

        return timezone;
    }
}
