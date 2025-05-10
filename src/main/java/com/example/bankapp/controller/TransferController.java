package com.example.bankapp.controller;

import com.example.bankapp.dto.TransferRequest;
import com.example.bankapp.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    @CacheEvict(value = { "accountByUserId" }, allEntries = true)
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        transferService.transfer(request.getFromUserId(), request.getToUserId(), request.getAmount());
        return ResponseEntity.ok("Transfer completed");
    }
}

