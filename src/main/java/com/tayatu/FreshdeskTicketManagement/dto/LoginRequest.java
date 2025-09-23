package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
