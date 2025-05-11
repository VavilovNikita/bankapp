package com.example.bankapp;

import com.example.bankapp.dto.TransferRequest;
import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.User;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
public class TransferServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("bankapp")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User userFrom;
    private User userTo;

    @Transactional
    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        Account fromAcc = new Account();
        fromAcc.setInitialBalance(BigDecimal.valueOf(1000));
        fromAcc.setBalance(BigDecimal.valueOf(1000));

        userFrom = new User();
        userFrom.setName("1");
        userFrom.setPassword("pass");
        userFrom.setAccount(fromAcc);

        userRepository.save(userFrom);

        Account toAcc = new Account();
        toAcc.setInitialBalance(BigDecimal.valueOf(100));
        toAcc.setBalance(BigDecimal.valueOf(100));

        userTo = new User();
        userTo.setName("2");
        userTo.setPassword("pass");
        userTo.setAccount(toAcc);

        userRepository.save(userTo);
    }

    @Test
    @WithMockUser(username = "1")
    void testTransferEndpoint() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setToUserId(userTo.getId());
        request.setAmount(BigDecimal.valueOf(200));

        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        BigDecimal fromBalance = accountRepository.findByUserId(userFrom.getId()).get().getBalance();
        BigDecimal toBalance = accountRepository.findByUserId(userTo.getId()).get().getBalance();

        assertThat(fromBalance).isEqualTo(BigDecimal.valueOf(800));
        assertThat(toBalance).isEqualTo(BigDecimal.valueOf(300));
    }
}
