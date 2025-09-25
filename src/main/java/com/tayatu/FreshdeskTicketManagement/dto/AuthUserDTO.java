package com.tayatu.FreshdeskTicketManagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class AuthUserDTO {
    private String username;
    private List<String> roles;
}