package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import com.tayatu.FreshdeskTicketManagement.model.User;
import com.tayatu.FreshdeskTicketManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<String> addUser(LoginRequest loginRequest) {
        if (userRepository.findByUsername(loginRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body("User already exists");
        }
        User user = new User();
        user.setUsername(loginRequest.getUsername());
        user.setPassword(loginRequest.getPassword());
//        user.setPassword(bCryptPasswordEncoder.encode(loginRequest.getPassword()));
        user.setRole(loginRequest.getRole());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully");
    }

}
