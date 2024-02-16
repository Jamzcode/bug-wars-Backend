package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.Bug;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Battleground {
    private Entity[][] grid;
    private List<Bug> bugs;
    private String name;
    private int index;
    private boolean isBytecodeValid;

    public Battleground(Entity[][] grid, List<Bug> bugs) {
        this.grid = grid;
        this.bugs = bugs;
    }

    public void executeCommand() {
    }
}
