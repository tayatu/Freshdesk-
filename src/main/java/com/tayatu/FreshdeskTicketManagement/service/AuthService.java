package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.dto.AuthUserDTO;
import com.tayatu.FreshdeskTicketManagement.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String baseUrl;

    public String loginUser(LoginRequest loginRequest) {
        String url = baseUrl + "/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    public ResponseEntity<AuthUserDTO> verifyUser(String jwtToken) {
        String url = baseUrl + "/verify-token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(jwtToken, headers);

        return restTemplate.postForEntity(url, entity, AuthUserDTO.class);
    }
}
