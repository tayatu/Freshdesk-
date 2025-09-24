package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.enums.RoleName;
import com.tayatu.FreshdeskTicketManagement.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Set;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Set<String> roles;
}
