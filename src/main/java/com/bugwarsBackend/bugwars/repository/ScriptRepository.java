package com.bugwarsBackend.bugwars.repository;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> getScriptsByUser(User user);

    Boolean scriptNameExists(String name);
}
