package com.tayatu.FreshdeskTicketManagement.dto;

import com.tayatu.FreshdeskTicketManagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String username;
    private Set<Role> roles;
    private String fullName;
    private String email;
    private String phoneNumber;
}
