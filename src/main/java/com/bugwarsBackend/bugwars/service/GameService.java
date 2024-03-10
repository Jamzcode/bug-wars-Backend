package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.game.Battleground;
import com.bugwarsBackend.bugwars.game.TickSummary;
import com.bugwarsBackend.bugwars.game.setup.BattlegroundFactory;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
public class GameService {

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;

    public static final int MAX_TICKS = 50;

    public void startGame(Long id, Principal principal) {
        // Retrieve user from the database
        Optional<User> user = userRepository.findByUsername(principal.getName());
        System.out.println("User" + principal.getName());
        if (user.isEmpty()) {
            // Handle case where user is not found
            System.out.println("User not found");
            return;
        }

        // Retrieve scripts for the user
        List<Script> scripts = scriptRepository.getScriptsByUser(user.get());
        //System.out.println("Scripts: " + scripts);
        Script userSelectScript = scripts.stream()
                .filter(script -> script.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Script not found for id: " + id));
        //System.out.println("This is user's script" + userSelectScript);
        // Load battleground and print the initial state
        Resource mapResource = new ClassPathResource("maps/tunnel.txt");
        BattlegroundFactory battlegroundFactory = new BattlegroundFactory(mapResource);
        Battleground battleground = battlegroundFactory.create();
        battleground.print();

        // Simulate ticks and print after each tick
        int[] dummyTicks = new int[MAX_TICKS]; //Setting 50 ticks for testing
        for (int i = 0; i < dummyTicks.length; i++) {
            TickSummary tickSummary = battleground.nextTick(userSelectScript);
            System.out.println("Tick: " + (i + 1));
            battleground.print();
            if (tickSummary.isGameOver()) {
                System.out.println("Game over!");
                break;
            }
        }
    }
}

