package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.dto.response.GameReplay;
import lombok.Data;

import java.util.List;

@Data
public class Game {
    private final Battleground battleground;
    private final List<Swarm> swarms;
    private final GameReplay replay;
    private final int maxMoves;

    public Game(Battleground battleground, List<Swarm> swarms, int maxMoves) {
        this.battleground = battleground;
        this.swarms = swarms;
        this.maxMoves = maxMoves;

        replay = new GameReplay(battleground.getName(), battleground.getGrid(), swarms);
    }

    public GameReplay play() {
        return replay;
    }
}

