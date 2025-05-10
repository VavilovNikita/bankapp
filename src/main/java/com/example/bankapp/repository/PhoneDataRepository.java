package com.example.bankapp.repository;

import com.example.bankapp.entity.PhoneData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    @Cacheable(value = "phoneDataByPhone", key = "#phone")
    Optional<PhoneData> findByPhone(String phone);
}

