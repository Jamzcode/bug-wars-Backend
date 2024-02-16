package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food implements Entity {
    private Point position;

    @Override
    public String toString() {
        return "F"; // Representation of food
    }
}
