package com.example.bankapp.repository;

import com.example.bankapp.entity.PhoneData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    boolean existsByPhone(String phone);
    long countByUserId(Long userId);
    @Cacheable(value = "phoneDataByPhone", key = "#phone")
    Optional<PhoneData> findByPhone(String phone);
}

