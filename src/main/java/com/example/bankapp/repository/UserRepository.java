package com.example.bankapp.repository;

import com.example.bankapp.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable(value = "userByEmail", key = "#email")
    Optional<User> findByEmailDataList_Email(String email);
    @Cacheable(value = "userByPhone", key = "#phone")
    Optional<User> findByPhoneDataList_Phone(String phone);

    Optional<User> findByName(String name);
}

