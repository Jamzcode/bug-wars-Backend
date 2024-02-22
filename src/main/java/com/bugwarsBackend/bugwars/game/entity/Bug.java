package com.bugwarsBackend.bugwars.game.entity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bug implements Entity {
    private final Map<Integer, Command> commands = new HashMap<>();
    private int swarm;
    private Point coords;
    private Direction direction;
    private int[] bytecode;
    private int index = 0;
    private int bugType;

    public Bug(Point coords, int swarm, int[] bytecode, Direction direction) {
        this.coords = coords;
        this.swarm = swarm;
        this.bytecode = bytecode;
        this.direction = direction;

        loadCommands();
    }

    public Bug(int bugType) {
        this.bugType = bugType;
        this.direction = Direction.NORTH; // Assuming NORTH is the default direction
    }
    private void loadCommands() {
        commands.put(30, this::ifEnemy);
        commands.put(31, this::ifAlly);
        commands.put(32, this::ifFood);
        commands.put(33, this::ifEmpty);
        commands.put(34, this::ifWall);
        commands.put(35, this::_goto);
    }

    public int determineAction(Entity frontEntity) {
        int result = -1;

        if (commands.containsKey(bytecode[index])) {
            Command command = commands.get(bytecode[index]);
            boolean success = command.execute(frontEntity);

            if (success) {
                index = bytecode[index + 1];
            } else {
                incrementIndex(2);
            }
        } else {
            result = bytecode[index];
            incrementIndex(1); // Increment the index here
        }
        return result;
    }

    private void incrementIndex(int increment) {
        index = (index + increment) % bytecode.length;
    }

    private boolean ifEnemy(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug enemyBug = (Bug) frontEntity;
            return enemyBug.swarm != swarm;
        }
        return false;
    }

    private boolean ifAlly(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug allyBug = (Bug) frontEntity;
            return allyBug.swarm == swarm;
        }
        return false;
    }

    private boolean ifFood(Entity frontEntity) {
        return frontEntity instanceof Food;
    }

    private boolean ifEmpty(Entity frontEntity) {
        return frontEntity == null;
    }

    private boolean ifWall(Entity frontEntity) {
        return frontEntity instanceof Wall;
    }

    private boolean _goto(Entity frontEntity) {
        return true;
    }

    @Override
    public Point getPosition() {
        return null;
    }

    @Override
    public void setPosition(Point position) {

    }

    @Override
    public String toString() {
        String color;
        if (swarm == 0) {
            color = "\033[0;34m"; // blue
        } else if (swarm == 1) {
            color = "\033[0;31m"; // red
        } else {
            throw new IllegalStateException("Unexpected value: " + swarm);
        }

        String directionString = ""; // Set directionString to an empty string

        String bugTypeString = (bugType == 0 || bugType == 1) ? String.valueOf(bugType) : ""; // Display bugType only if it's 0 or 1

        return String.format("%s%s%s\033[0m", color, bugTypeString, "\033[0m");
    }

    public String bugTeam() {
        return String.valueOf(bugType); // Return the bugType as a string
    }

    public Point getCoords() {
        return coords;
    }

    public int getSwarm() {
        return swarm;
    }

    // FunctionalInterface used to enforce single abstract method
    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }
}
