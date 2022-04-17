package com.istiak.timezone.repository;

import com.istiak.timezone.model.Timezone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimezoneRepository extends JpaRepository<Timezone, Long> {
    //Page<Timezone> findByUserId(Long userId, Pageable pageable);
    Timezone findByName(String name);
}
