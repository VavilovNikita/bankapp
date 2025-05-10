package com.example.bankapp.repository;

import com.example.bankapp.entity.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Cacheable(value = "accountByUserId", key = "#userId")
    Optional<Account> findByUserId(Long userId);
}

