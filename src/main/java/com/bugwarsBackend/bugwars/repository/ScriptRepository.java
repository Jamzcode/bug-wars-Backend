package com.bugwarsBackend.bugwars.repository;

import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> getScriptsByUser(User user);

    @Query("SELECT new com.bugwarsBackend.bugwars.dto.response.ScriptName(s.id, s.name, u.username) " +
            "FROM Script s " +
            "JOIN s.user u " +
            "WHERE s.isBytecodeValid = true")
    List<Script> getAllValidScripts();

    Boolean existsByName(String name);
}
