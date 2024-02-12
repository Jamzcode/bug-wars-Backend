package com.bugwarsBackend.bugwars.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Food implements Entity {
    @Override
    public String toString() {
        return "F";
    }
}

