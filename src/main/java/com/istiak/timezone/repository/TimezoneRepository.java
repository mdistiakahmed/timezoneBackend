package com.istiak.timezone.repository;

import com.istiak.timezone.model.Timezone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimezoneRepository extends JpaRepository<Timezone, Long> {
    Page<Timezone> findByEmail(String email, Pageable pageable);
    Timezone findByName(String name);
    Long deleteByName(String name);
}
