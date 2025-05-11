package com.example.bankapp.service;

import com.example.bankapp.entity.EmailData;
import com.example.bankapp.entity.PhoneData;
import com.example.bankapp.entity.User;
import com.example.bankapp.repository.EmailDataRepository;
import com.example.bankapp.repository.PhoneDataRepository;
import com.example.bankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDataService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailDataRepository emailDataRepository;
    @Autowired
    private PhoneDataRepository phoneDataRepository;

    @Transactional
    public void addEmail(Long userId, String email) {
        if (emailDataRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = getUserOrThrow(userId);
        EmailData newEmail = new EmailData();
        newEmail.setUser(user);
        newEmail.setEmail(email);
        emailDataRepository.save(newEmail);
    }

    @Transactional
    public void updateEmail(Long userId, Long emailId, String newEmail) {
        EmailData emailData = emailDataRepository.findById(emailId)
            .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("You can't update this email");
        }
        if (emailDataRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already in use");
        }
        emailData.setEmail(newEmail);
        emailDataRepository.save(emailData);
    }

    @Transactional
    public void deleteEmail(Long userId, Long emailId) {
        EmailData emailData = emailDataRepository.findById(emailId)
            .orElseThrow(() -> new IllegalArgumentException("Email not found"));
        if (!emailData.getUser().getId().equals(userId)) {
            throw new SecurityException("You can't delete this email");
        }

        long count = emailDataRepository.countByUserId(userId);
        if (count <= 1) {
            throw new IllegalStateException("User must have at least one email");
        }

        emailDataRepository.delete(emailData);
    }

    @Transactional
    public void addPhone(Long userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Phone already in use");
        }
        User user = getUserOrThrow(userId);
        PhoneData newPhone = new PhoneData();
        newPhone.setUser(user);
        newPhone.setPhone(phone);
        phoneDataRepository.save(newPhone);
    }

    @Transactional
    public void updatePhone(Long userId, Long phoneId, String newPhone) {
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
            .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("You can't update this phone");
        }
        if (phoneDataRepository.existsByPhone(newPhone)) {
            throw new IllegalArgumentException("Phone already in use");
        }
        phoneData.setPhone(newPhone);
        phoneDataRepository.save(phoneData);
    }

    @Transactional
    public void deletePhone(Long userId, Long phoneId) {
        PhoneData phoneData = phoneDataRepository.findById(phoneId)
            .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
        if (!phoneData.getUser().getId().equals(userId)) {
            throw new SecurityException("You can't delete this phone");
        }

        long count = phoneDataRepository.countByUserId(userId);
        if (count <= 1) {
            throw new IllegalStateException("User must have at least one phone");
        }

        phoneDataRepository.delete(phoneData);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

