package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;

public class GameController {
    @Autowired
    GameService gameService;
}
