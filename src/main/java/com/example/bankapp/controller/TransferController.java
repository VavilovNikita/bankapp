package com.example.bankapp.controller;

import com.example.bankapp.dto.TransferRequest;
import com.example.bankapp.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Transfer Money", description = "Transfer funds from one user to another")
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid transfer details")
    @PostMapping
    @CacheEvict(value = { "accountByUserId" }, allEntries = true)
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        transferService.transfer(request.getToUserId(), request.getAmount());
        return ResponseEntity.ok("Transfer completed");
    }
}

