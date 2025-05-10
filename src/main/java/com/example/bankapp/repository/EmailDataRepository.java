package com.example.bankapp.repository;

import com.example.bankapp.entity.EmailData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    @Cacheable(value = "emailDataByEmail", key = "#email")
    Optional<EmailData> findByEmail(String email);
}

