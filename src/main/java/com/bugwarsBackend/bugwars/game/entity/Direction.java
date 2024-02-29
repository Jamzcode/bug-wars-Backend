package com.bugwarsBackend.bugwars.game.entity;

import java.awt.*;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction turnLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
            case SOUTH -> EAST;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "^";
            case EAST -> ">";
            case SOUTH -> "v";
            case WEST -> "<";
        };
    }

    public Point goForward(Point coords) {
        return switch (this) {
            case NORTH -> new Point(coords.x, coords.y - 1);
            case EAST -> new Point(coords.x + 1, coords.y);
            case SOUTH -> new Point(coords.x, coords.y + 1);
            case WEST -> new Point(coords.x - 1, coords.y);
        };
    }
}