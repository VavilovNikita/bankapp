package com.example.bankapp.controller;

import com.example.bankapp.dto.LoginRequest;
import com.example.bankapp.dto.RegisterRequest;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.EmailData;
import com.example.bankapp.entity.User;
import com.example.bankapp.repository.UserRepository;
import com.example.bankapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmailDataList_Email(request.getEmail());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtTokenProvider.createToken(userOpt.get().getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmailDataList_Email(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = new User();
        user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        user.setName(request.getName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPassword(request.getPassword());

        EmailData email = new EmailData(
            UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
            user,
            request.getEmail()
        );
        user.setEmailDataList(List.of(email));

        Account account = new Account(
            UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE,
            user,
            BigDecimal.ZERO
        );
        user.setAccount(account);

        userRepository.save(user);

        return ResponseEntity.ok("User registered");
    }
}

