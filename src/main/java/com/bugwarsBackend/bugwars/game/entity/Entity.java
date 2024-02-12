package com.bugwarsBackend.bugwars.game.entity;

import java.awt.*;

public interface Entity {
    void setPosition(Point position);
    Point getPosition();
    String toString();
}
