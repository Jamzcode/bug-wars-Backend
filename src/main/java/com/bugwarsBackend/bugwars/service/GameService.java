package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GameService {

    @Autowired
    ScriptRepository scriptRepository;
}
