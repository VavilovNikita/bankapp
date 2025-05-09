package com.example.bankapp.repository;

import com.example.bankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailDataList_Email(String email);
    Optional<User> findByPhoneDataList_Phone(String phone);
}

