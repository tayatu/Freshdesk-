package com.tayatu.FreshdeskTicketManagement.service;

import com.tayatu.FreshdeskTicketManagement.enums.RoleName;
import com.tayatu.FreshdeskTicketManagement.model.Role;
import com.tayatu.FreshdeskTicketManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HomeService {

    @Autowired
    private RoleRepository roleRepository;

    public void addRoles() {

        Set<Role> roleSet = Set.of(
                new Role(RoleName.ROLE_ADMIN),
                new Role(RoleName.ROLE_AGENT),
                new Role(RoleName.ROLE_CUSTOMER),
                new Role(RoleName.ROLE_USER)
        );

        roleSet.forEach(role -> {
            if (roleRepository.findByName(role.getName()).isPresent()) {
                roleRepository.save(role);
            }
        });
    }
}
