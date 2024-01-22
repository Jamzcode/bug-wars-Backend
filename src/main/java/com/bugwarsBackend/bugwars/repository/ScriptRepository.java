package com.bugwarsBackend.bugwars.repository;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> findScriptByUser(User user);

    Boolean scriptExists(String name);
}
