package com.example.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class PhoneRequest {
    @NotBlank
    @Pattern(regexp = "\\+?[0-9]{7,15}")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
