package com.tayatu.FreshdeskTicketManagement.repository;

import com.tayatu.FreshdeskTicketManagement.enums.RoleName;
import com.tayatu.FreshdeskTicketManagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
