package com.istiak.timezone.service;

import com.istiak.timezone.model.*;
import com.istiak.timezone.repository.TimezoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class TimezoneService {
    @Autowired
    private TimezoneRepository timezoneRepository;


    public TimezoneTableResponseModel getAllUserTimeZone(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Timezone> timezones = timezoneRepository.findAll(pageable);

        return mapToResponse(timezones);
    }


    public TimezoneTableResponseModel getUserTimeZone(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) authentication.getPrincipal();
        Page<Timezone> timezones = timezoneRepository.findByEmail(userEmail, pageable);

        return mapToResponse(timezones);
    }

    public void createTimezone(Timezone timezone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (String) authentication.getPrincipal();
        timezone.setEmail(userEmail);
        timezoneRepository.saveAndFlush(timezone);
    }

    public void updateTimezone(TimeZoneDataModel timeZoneDataModel) {
        Timezone updatedValue = TimeZoneDataModel.of(timeZoneDataModel);
        Timezone existingValue = timezoneRepository.findByName(timeZoneDataModel.getName());
        if(existingValue == null) return;

        updatedValue.setId(existingValue.getId());
        updatedValue.setEmail(existingValue.getEmail());
        timezoneRepository.saveAndFlush(updatedValue);
    }


    public void deleteTimezoneByName(String name) {
        timezoneRepository.deleteByName(name);
    }



    private TimezoneTableResponseModel mapToResponse(Page<Timezone> timezones) {
        List<TimeZoneDataModel> timeZoneDataModelList = timezones.getContent()
                .stream()
                .map((e) -> TimeZoneDataModel.from(e))
                .collect(Collectors.toList());

        TimezoneTableResponseModel timezoneTableResponse = new TimezoneTableResponseModel();
        timezoneTableResponse.setTimeZoneDataModelList(timeZoneDataModelList);
        timezoneTableResponse.setPageNo(timezones.getNumber());
        timezoneTableResponse.setPageSize(timezones.getSize());
        timezoneTableResponse.setTotalElements(timezones.getTotalElements());
        timezoneTableResponse.setTotalPages(timezones.getTotalPages());

        return timezoneTableResponse;
    }
}
