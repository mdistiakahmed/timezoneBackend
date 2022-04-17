package com.istiak.timezone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimezoneTableResponseModel {
    private List<TimeZoneDataModel> timeZoneDataModelList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
