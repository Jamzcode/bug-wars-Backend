package com.bugwarsBackend.bugwars.game;

import lombok.Data;

import java.awt.Point;

@Data
public class ActionSummary {
    private final Point coords;
    private final int action;

    public ActionSummary(Point coords, int action) {
        this.coords = coords;
        this.action = action;
    }

    public Integer getSwarm() {
        return action;
    }
}



