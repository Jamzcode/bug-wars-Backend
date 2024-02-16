package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor
public class EmptySpace implements Entity {
    private Point position;

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return " ";
    }
}
