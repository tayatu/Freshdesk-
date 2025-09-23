package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserProfileDTO {
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String fullName;
    private String email;
    private String phoneNumber;
}
