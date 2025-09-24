package com.tayatu.FreshdeskTicketManagement.util;

import com.tayatu.FreshdeskTicketManagement.enums.RoleName;

public class RoleUtils {
    public static RoleName getRoleNameOrThrow(String role) {
        try {
            return RoleName.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + role);
        }
    }
}