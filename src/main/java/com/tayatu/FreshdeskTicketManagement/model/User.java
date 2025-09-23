package com.tayatu.FreshdeskTicketManagement.model;

import com.tayatu.FreshdeskTicketManagement.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    private String fullName;
    private String email;
    private String phoneNumber;

}