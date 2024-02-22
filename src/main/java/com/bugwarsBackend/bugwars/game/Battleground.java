package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.Entity;

import java.awt.*;

public class Battleground {
    private String name;
    private Entity[][] grid;

    public Battleground(String name, Entity[][] grid) {
        this.name = name;
        this.grid = grid;
    }

    public void print() {
        for (Entity[] entities : grid) {
            for (Entity e : entities) {
                if (e == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(e);
                }
            }
            System.out.println();
        }
    }

    private Entity getEntityAtCoords(Point coords) {
        return grid[coords.y][coords.x];
    }
}
