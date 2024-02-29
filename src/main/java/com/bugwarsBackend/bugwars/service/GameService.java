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

import java.util.List;
import java.util.Optional;


@Service
public class GameService {

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;

    public static final int MAX_TICKS = 50;

    public void startGame(String username) {
        // Retrieve user from the database
        Optional<User> user = userRepository.findByUsername(username);
        if (user == null) {
            // Handle case where user is not found
            return;
        }

        // Retrieve scripts for the user
        List<Script> scripts = scriptRepository.getScriptsByUser(user.get());

        // Load battleground and print the initial state
        Resource mapResource = new ClassPathResource("maps/tunnel.txt");
        BattlegroundFactory battlegroundFactory = new BattlegroundFactory(mapResource);
        Battleground battleground = battlegroundFactory.printGrid();
        battleground.print();

        // Simulate ticks and print after each tick
        int[] dummyTicks = {0};
        for (int i = 0; i < dummyTicks.length; i++) {
            TickSummary tickSummary = battleground.nextTick();
            System.out.println("Tick: " + (i + 1));
            battleground.print();
            if (tickSummary.isGameOver()) {
                System.out.println("Game over!");
                break;
            }
        }
    }
}

