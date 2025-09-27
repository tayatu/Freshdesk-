package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.dto.UserProfileDTO;
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
import java.util.List;
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

    public ResponseEntity<List<UserProfileDTO>> getAllAgents() {
        Role agentRole = roleRepository.findByName(RoleName.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Role not found: " + RoleName.ROLE_AGENT));
        List<User> agents = userRepository.findByRolesIn(Set.of(agentRole));
        List<UserProfileDTO> agentDTOs = agents.stream().map(user -> {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getFullName());
            dto.setRoles(user.getRoles());
            return dto;
        }).toList();
        return ResponseEntity.ok(agentDTOs);
    }

    public ResponseEntity<UserProfileDTO> getAgentById(Long id) {
        User agent = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));
        Role agentRole = roleRepository.findByName(RoleName.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Role not found: " + RoleName.ROLE_AGENT));
        if (!agent.getRoles().contains(agentRole)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(agent.getUsername());
        dto.setEmail(agent.getEmail());
        dto.setFullName(agent.getFullName());
        dto.setRoles(agent.getRoles());
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<String> updateAgent(Long id, User agent) {
        User existingAgent = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));
        Role agentRole = roleRepository.findByName(RoleName.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Role not found: " + RoleName.ROLE_AGENT));
        if (!existingAgent.getRoles().contains(agentRole)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent not found with id: " + id);
        }

        if (!StringUtils.isEmpty(agent.getUsername())) {
            existingAgent.setUsername(agent.getUsername());
        }
        if (!StringUtils.isEmpty(agent.getPassword())) {
            existingAgent.setPassword(agent.getPassword());
        }
        if (!StringUtils.isEmpty(agent.getEmail())) {
            existingAgent.setEmail(agent.getEmail());
        }
        if (!StringUtils.isEmpty(agent.getFullName())) {
            existingAgent.setFullName(agent.getFullName());
        }

       if (agent.getRoles() != null && !agent.getRoles().isEmpty()) {
              Set<Role> persistentRoles = new HashSet<>();
              for (Role transientRole : agent.getRoles()) {
                RoleName roleName = RoleUtils.getRoleNameOrThrow(transientRole.getName().name());
                Role role = roleRepository.findByName(roleName)
                          .orElseThrow(() -> new RuntimeException("Role not found: " + transientRole.getName()));
                persistentRoles.add(role);
              }
              existingAgent.setRoles(persistentRoles);
       }

        userRepository.save(existingAgent);
        return ResponseEntity.ok("Agent updated successfully");
    }

    public ResponseEntity<String> deleteAgent(Long id) {
        User existingAgent = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));
        Role agentRole = roleRepository.findByName(RoleName.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Role not found: " + RoleName.ROLE_AGENT));
        if (!existingAgent.getRoles().contains(agentRole)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent not found with id: " + id);
        }
        // remove the agent role before saving the user
        existingAgent.getRoles().remove(agentRole);
        userRepository.save(existingAgent);
        return ResponseEntity.ok("Agent deleted successfully");
    }
}
