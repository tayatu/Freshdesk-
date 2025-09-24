package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.enums.RoleName;
import com.tayatu.FreshdeskTicketManagement.model.Role;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.RoleRepository;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import com.tayatu.FreshdeskTicketManagement.util.RoleUtils;
import com.tayatu.FreshdeskTicketManagement.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    public ResponseEntity<String> createAdmin(LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getUsername()) || StringUtils.isEmpty(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username and password cannot be empty.");
        }
        if (userRepository.findByUsername(loginRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body("User already exists");
        }

        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());

        Set<Role> persistentRoles = new HashSet<>();
        for (String transientRole : loginRequest.getRoles()) {
            RoleName roleName = RoleUtils.getRoleNameOrThrow(transientRole);
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + transientRole));
            persistentRoles.add(role);
        }
        user.setRoles(persistentRoles);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    public ResponseEntity<String> createAgent(User agent) {
        if (StringUtils.isEmpty(agent.getUsername()) || StringUtils.isEmpty(agent.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username and password cannot be empty.");
        }
        if (userRepository.findByUsername(agent.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body("Agent already exists");
        }

        Role agentRole = roleRepository.findByName(RoleName.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Role not found: " + RoleName.ROLE_AGENT));
        agent.setRoles(Set.of(agentRole));

        userRepository.save(agent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Agent created successfully");
    }
}
