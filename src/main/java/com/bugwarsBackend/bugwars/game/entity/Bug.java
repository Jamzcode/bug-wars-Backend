package com.bugwarsBackend.bugwars.game.entity;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.service.ScriptService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class Bug implements Entity {
    private final Map<Integer, Command> commands = new HashMap<>();
    private int swarm;
    private Point coords;
    private Direction direction;
    private int[] userBytecode;
    private int index = 0;
    private int bugType;

    @Autowired
    ScriptService scriptService;

    public Bug(Point coords, int swarm, int[] userBytecode, Direction direction, int bugType) {
        this.coords = coords;
        this.swarm = swarm;
        this.userBytecode = userBytecode;
        this.direction = direction;
        this.bugType = bugType;

        loadCommands();
    }

    public Bug(int swarm, Point coords) {
        this.swarm = swarm;
        this.coords = coords;
    }

    public Bug(Direction direction) {
        this.direction = direction;
    }

    public Bug(int[] userBytecode) {
        this.userBytecode = userBytecode;
    }
    public Bug(int swarm) {
        this.swarm = swarm;
    }


    public Bug(Point coords) {
        this.coords = coords;
    }


    public Direction getDirection() {
        return direction;
    }

    public Bug(int bugType, int[] userBytecode) {
        this.bugType = bugType;
        this.coords = new Point();
        this.direction = Direction.NORTH;
        this.userBytecode = userBytecode;
    }

    private void loadCommands() {
        commands.put(30, this::ifEnemy);
        commands.put(31, this::ifAlly);
        commands.put(32, this::ifFood);
        commands.put(33, this::ifEmpty);
        commands.put(34, this::ifWall);
        commands.put(35, this::_goto);
    }

    public int determineAction(Entity frontEntity, Script script) {
        int result = -1;
        System.out.println(script.getBytecode());
        int[] userBytecode  = script.getBytecode();
        if (commands.containsKey(userBytecode[index])) {
            System.out.println("userBytecode: " + userBytecode[index]);
            Command command = commands.get(userBytecode[index]);
            boolean success = command.execute(frontEntity);

            if (success) {
                index = userBytecode[index + 1];
            } else {
                incrementIndex(2);
            }
        } else {
            result = userBytecode[index];
            incrementIndex(1); // Increment the index here
        }
        return result;
    }

    private void incrementIndex(int increment) {
        index = (index + increment) % userBytecode.length;
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

    // FunctionalInterface used to enforce single abstract method
    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }
}
