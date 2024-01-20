package com.bugwarsBackend.bugwars.repository;

import com.bugwarsBackend.bugwars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
