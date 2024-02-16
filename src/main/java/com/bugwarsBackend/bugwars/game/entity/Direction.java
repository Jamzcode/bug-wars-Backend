package com.bugwarsBackend.bugwars.game.entity;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction faceNorth() {

    }

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
}