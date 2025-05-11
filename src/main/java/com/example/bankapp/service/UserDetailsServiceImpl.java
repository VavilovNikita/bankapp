package com.example.bankapp.service;

import com.example.bankapp.entity.User;
import com.example.bankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Загружает пользователя по email или телефону.
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmailDataList_Email(identifier);

        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByPhoneDataList_Phone(identifier);
        }

        User user = userOpt.orElseThrow(() ->
            new UsernameNotFoundException("User not found with email or phone: " + identifier));

        return org.springframework.security.core.userdetails.User
            .withUsername(identifier)
            .password(user.getPassword())
            .authorities("USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getId().toString())
            .password(user.getPassword())
            .authorities("USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
