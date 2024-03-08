package com.bugwarsBackend.bugwars.game.entity;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.service.ScriptService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class Bug implements Entity {
    private final Map<Integer, Command> commands = new HashMap<>();
    private int swarm;
    private Point coords;
    @Getter
    private Direction direction;
    private int[] userBytecode;
    private int index = 0;
    private int bugType;

    //test
    private boolean moved = false;
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
        //System.out.println("Script: " + script);
        userBytecode  = script.getBytecode(); //changed int[] to userBytecode that was instantiated at the top

        loadCommands(); //loaded commands into commands

        for (int i = 0; i < userBytecode.length; i++) {
            if (commands.containsKey(userBytecode[i])) { //loading commands allows us to compare the userBytecode to the commands
                //System.out.println("userBytecode: " + userBytecode[i]);
                Command command = commands.get(userBytecode[i]);
                boolean success = command.execute(frontEntity); //not really sure what this does

                if (success) {
                    index = userBytecode[i + 1];
                } else {
                    incrementIndex(2);
                }
            } else {
                result = userBytecode[i];
                incrementIndex(1); // Moved incrementIndex back here
            }
        }
        return result;
    }

//    public int determineAction(Entity frontEntity, Script script) {
//        int result = -1;
//        System.out.println("Script: " + script);
//        userBytecode = script.getBytecode();
//
//        loadCommands();
//
//        for (int i = 0; i < userBytecode.length; i++) {
//            if (commands.containsKey(userBytecode[i])) {
//                Command command = commands.get(userBytecode[i]);
//                boolean success = command.execute(frontEntity);
//
//                if (success) {
//                    if (userBytecode[i] == 0) { // If the command is a NOOP, increment index by 1
//                        incrementIndex(1);
//                    } else if (userBytecode[i] >= 10 && userBytecode[i] <= 14) { // If the command is a valid action
//                        incrementIndex(1); // Increment index by 1 to skip the next bytecode, assuming it represents the parameter for the action
//                    } else {
//                        throw new RuntimeException("Invalid action: " + userBytecode[i]);
//                    }
//                } else {
//                    incrementIndex(2); // Increment index by 2 if the command execution fails
//                }
//            } else {
//                result = userBytecode[i]; // Set the result to the current bytecode if it's not a recognized command
//                incrementIndex(1);
//            }
//        }
//        return result;
//    }


    private void incrementIndex(int increment) {
        //index = (index + increment) % userBytecode.length;

        if (userBytecode != null && userBytecode.length > 0) {
            index = (index + increment) % userBytecode.length;
        } else {
            throw new IllegalArgumentException("userBytecode is null or empty");
        }

        if (index >= userBytecode.length) {
            throw new IllegalArgumentException("index is out of bounds");

        }
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

//    private boolean _goto(Entity frontEntity) {
//        return true;
//    }

private boolean _goto(Entity frontEntity) {
    // Implement the logic for determining whether the bug should move to the specified location
    // This method should return true if the bug should move, false otherwise
    // You need to determine the conditions under which the bug should move based on your game's requirements
    if (frontEntity instanceof Food) {
        // Move to the location if it contains food
        return true;
    } else if (frontEntity instanceof Wall) {
        // Do not move if the location contains a wall
        return false;
    } else if (frontEntity instanceof Bug) {
        // Move to the location if it contains an enemy bug
        Bug enemyBug = (Bug) frontEntity;
        return enemyBug.getSwarm() != swarm;
    } else {
        // Move to the location by default if no specific condition is met
        return true;
    }
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
        String bugTypeString = "";

        if (swarm == 0) {
            color = "\033[0;34m"; // blue
            bugTypeString = "0";
        } else if (swarm == 1) {
            color = "\033[0;31m"; // red
            bugTypeString = "1";
        } else {
            throw new IllegalStateException("Unexpected value: " + swarm);
        }

        return String.format("%s%s\033[0m", color, bugTypeString);
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
