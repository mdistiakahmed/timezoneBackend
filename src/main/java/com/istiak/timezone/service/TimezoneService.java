package com.istiak.timezone.service;

import com.istiak.timezone.model.*;
import com.istiak.timezone.repository.TimezoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class TimezoneService {
    @Autowired
    private TimezoneRepository timezoneRepository;

    public TimezoneResponse getAllTimeZone(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Timezone> mapToResponse = timezoneRepository.findAll(pageable);

        return mapToResponse(mapToResponse);
    }

    public TimezoneResponse getUserTimeZone(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Timezone> timezones = timezoneRepository.findByUserId(userId, pageable);

        return mapToResponse(timezones);
    }

    public Timezone getSingleTimeZone(Long id) {
        Optional<Timezone> timezone = timezoneRepository.findById(id);
        return timezone.get();
    }

    public void saveTimezone(Timezone timezone) {
        Timezone existingEntry = timezoneRepository.findByName(timezone.getName());
        if(existingEntry != null) {
            //Update
            timezone.setId(existingEntry.getId());
        }
        timezoneRepository.saveAndFlush(timezone);

    }


    public void deleteTimezoneById(Long id) {
        timezoneRepository.deleteById(id);
    }


    private TimezoneResponse mapToResponse(Page<Timezone> timezones) {
        TimezoneResponse timezoneResponse = new TimezoneResponse();
        timezoneResponse.setTimezoneList(timezones.getContent());
        timezoneResponse.setPageNo(timezones.getNumber());
        timezoneResponse.setPageSize(timezones.getSize());
        timezoneResponse.setTotalElements(timezones.getTotalElements());
        timezoneResponse.setTotalPages(timezones.getTotalPages());
        timezoneResponse.setLast(timezones.isLast());

        return timezoneResponse;
    }
}
