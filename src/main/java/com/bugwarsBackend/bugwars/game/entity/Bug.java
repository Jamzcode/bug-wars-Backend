package com.bugwarsBackend.bugwars.game.entity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Bug implements Entity {
    private Map<Integer, Command> commands = new HashMap<>();
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
        if (frontEntity instanceof Food) {
            return true;
        }
        return false;
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
        String color = switch (swarm) {
            case 0 -> "\033[0;34m"; // blue
            case 1 -> "\033[0;31m"; // red
            default -> throw new IllegalStateException("Unexpected value: " + swarm);
        };
        String directionString = (direction != null) ? direction.toString() : "No direction"; // Check for null before invoking toString
        return String.format("%s%s%s", color, directionString, "\033[0m");
    }

    public String bugTeam() {
        return String.valueOf(bugType); // Return the bugType as a string
    }

    // FunctionalInterface used to enforce single abstract method
    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }
}
