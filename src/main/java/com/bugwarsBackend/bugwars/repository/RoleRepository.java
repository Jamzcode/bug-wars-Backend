package com.bugwarsBackend.bugwars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.Optional;

@Repository
public interface RoleRepository<ERole> extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
