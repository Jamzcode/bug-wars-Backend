package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wall implements Entity {
    private Point position;

    @Override
    public Point getPosition() {
        return position;
    }
    @Override
    public String toString() {
        return "X";
    }
}