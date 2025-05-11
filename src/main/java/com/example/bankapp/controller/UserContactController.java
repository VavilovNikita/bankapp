package com.example.bankapp.controller;

import com.example.bankapp.dto.EmailRequest;
import com.example.bankapp.dto.PhoneRequest;
import com.example.bankapp.entity.UserPrincipal;
import com.example.bankapp.service.UserDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/contact")
public class UserContactController {
    @Autowired
    private UserDataService userDataService;

    @Operation(summary = "Add Email", description = "Add a new email for the user")
    @ApiResponse(responseCode = "200", description = "Email added successfully")
    @PostMapping("/email")
    public ResponseEntity<Void> addEmail(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestBody @Valid EmailRequest request) {
        userDataService.addEmail(user.getId(), request.getValue());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update Email", description = "Update an existing email for the user")
    @ApiResponse(responseCode = "200", description = "Email updated successfully")
    @PutMapping("/email/{id}")
    public ResponseEntity<Void> updateEmail(@AuthenticationPrincipal UserPrincipal user,
                                            @PathVariable Long id,
                                            @RequestBody @Valid EmailRequest request) {
        userDataService.updateEmail(user.getId(), id, request.getValue());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Email", description = "Delete an email for the user")
    @ApiResponse(responseCode = "200", description = "Email deleted successfully")
    @DeleteMapping("/email/{id}")
    public ResponseEntity<Void> deleteEmail(@AuthenticationPrincipal UserPrincipal user,
                                            @PathVariable Long id) {
        userDataService.deleteEmail(user.getId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add Phone", description = "Add a new phone number for the user")
    @ApiResponse(responseCode = "200", description = "Phone added successfully")
    @PostMapping("/phone")
    public ResponseEntity<Void> addPhone(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestBody @Valid PhoneRequest request) {
        userDataService.addPhone(user.getId(), request.getValue());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update Phone", description = "Update an existing phone number for the user")
    @ApiResponse(responseCode = "200", description = "Phone updated successfully")
    @PutMapping("/phone/{id}")
    public ResponseEntity<Void> updatePhone(@AuthenticationPrincipal UserPrincipal user,
                                            @PathVariable Long id,
                                            @RequestBody @Valid PhoneRequest request) {
        userDataService.updatePhone(user.getId(), id, request.getValue());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete Phone", description = "Delete a phone number for the user")
    @ApiResponse(responseCode = "200", description = "Phone deleted successfully")
    @DeleteMapping("/phone/{id}")
    public ResponseEntity<Void> deletePhone(@AuthenticationPrincipal UserPrincipal user,
                                            @PathVariable Long id) {
        userDataService.deletePhone(user.getId(), id);
        return ResponseEntity.ok().build();
    }
}
