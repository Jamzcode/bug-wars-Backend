package com.bugwarsBackend.bugwars.game;

import lombok.Data;

import java.awt.*;

/*
public records are used to create immutable objects.
represent DTOs & when you want to create classes that are used for data storage.
 */
import java.awt.Point;

@Data
public class ActionSummary {
    private final Point coords;
    private final int action;

    public ActionSummary(Point coords, int action) {
        this.coords = coords;
        this.action = action;
    }

}


