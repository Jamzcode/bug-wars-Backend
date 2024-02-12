package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.awt.Point;

@Data
@AllArgsConstructor
public class Wall implements Entity {
    private Point position;

    @Override
    public String toString() {
        return "X";
    }
}