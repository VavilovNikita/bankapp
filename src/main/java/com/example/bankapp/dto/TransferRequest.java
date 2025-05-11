package com.example.bankapp.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private Long toUserId;
    private BigDecimal amount;

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
