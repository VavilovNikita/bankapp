package com.example.bankapp.repository;

import com.example.bankapp.entity.EmailData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    boolean existsByEmail(String email);
    long countByUserId(Long userId);
    @Cacheable(value = "emailDataByEmail", key = "#email")
    Optional<EmailData> findByEmail(String email);
}

