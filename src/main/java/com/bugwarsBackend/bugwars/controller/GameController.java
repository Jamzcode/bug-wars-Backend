package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.dto.request.GameRequest;
import com.bugwarsBackend.bugwars.dto.request.ScriptRequest;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/gameStart")
public class GameController {
    @Autowired
    GameService gameService;

    @PostMapping (path = "/{id}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createGame(@PathVariable Long id, Principal principal) {
        gameService.startGame(id, principal);
    }
}
