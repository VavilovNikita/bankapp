package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service

public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private Long getAuthenticatedUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.valueOf(userDetails.getUsername());
    }

    @Transactional
    public void transfer(Long toUserId, BigDecimal amount) {
        Long fromUserId = getAuthenticatedUserId();

        log.info("Начинается перевод: от userId={} к userId={}, сумма={}", fromUserId, toUserId, amount);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Попытка перевода недопустимой суммы: {}", amount);
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account from = accountRepository.findByUserId(fromUserId)
            .orElseThrow(() -> {
                log.error("Аккаунт отправителя (userId={}) не найден", fromUserId);
                return new RuntimeException("Sender account not found");
            });

        Account to = accountRepository.findByUserId(toUserId)
            .orElseThrow(() -> {
                log.error("Аккаунт получателя (userId={}) не найден", toUserId);
                return new RuntimeException("Receiver account not found");
            });

        if (from.getBalance().compareTo(amount) < 0) {
            log.warn("Недостаточно средств на аккаунте userId={}. Баланс={}, сумма={}",
                fromUserId, from.getBalance(), amount);
            throw new RuntimeException("Insufficient balance");
        }

        synchronized (from) {
            synchronized (to) {
                from.setBalance(from.getBalance().subtract(amount));
                to.setBalance(to.getBalance().add(amount));

                accountRepository.save(from);
                accountRepository.save(to);

                log.info("Перевод завершён: от userId={} к userId={}, сумма={}", fromUserId, toUserId, amount);
            }
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void incrementBalance() {
        log.info("Начинается начисление процентов на счета пользователей");
        userRepository.findAll().forEach(user -> {
            BigDecimal currentBalance = user.getAccount().getBalance();
            BigDecimal initialBalance = user.getAccount().getInitialBalance();
            BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.10));

            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
            }

            user.getAccount().setBalance(newBalance);
            userRepository.save(user);

            log.debug("Пользователь userId={} — новый баланс: {}", user.getId(), newBalance);
        });
        log.info("Завершено начисление процентов на балансы пользователей");
    }
}
