package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.*;
import com.bugwarsBackend.bugwars.game.setup.TurnOrderCalculator;
import com.bugwarsBackend.bugwars.model.Script;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Battleground {
    private List<Bug> bugs;
    private String name;
    private int swarm;
    private Entity[][] grid;
    private final Map<Integer, Action> actions = new HashMap<>();
    private final Map<Integer, Command> commands = new HashMap<>();
    private List<Bug> turnOrder;

    private final List<Integer> actionsToBeTaken = new ArrayList<>();
    //List of Action Codes - 0, 31, 0, 29
    //Create a static map that maps the code to the action
        //New Hashmap({Map<Integer, Action>)
    private int index;

    public Battleground(String name, Entity[][] grid, List<Bug> bugs) {
        this.name = name;
        this.grid = grid;
        if (bugs != null) {
            this.bugs = new ArrayList<>(bugs);
        } else {
            this.bugs = new ArrayList<>();
        }

        init();
        loadCommands();

        updateGrid();
    }



//    public void print() {
//        for (Entity[] entities : grid) {
//            for (Entity entity : entities) {
//                if (entity == null) {
//                    System.out.print(" ");
//                } else {
//                    System.out.print(entity);
//                }
//            }
//            System.out.println();
//        }
//    }

//    public TickSummary nextTick(Script script) {
//
//        int[] bytecode = Arrays.copyOf(script.getBytecode(), script.getBytecode().length);
//        // Calculate turn order
//        TurnOrderCalculator turnOrderCalculator = new TurnOrderCalculator(grid, null); // You might need to pass swarms if required
//        List<Bug> turnOrder = turnOrderCalculator.calculateTurnOrder();
//        System.out.print("Turn order: " + turnOrder);
//        System.out.println("Turn order size: " + turnOrder.size());
//
//        // Execute actions in the turn order
//        List<ActionSummary> actionsTaken = new ArrayList<>();
//        for(int action : bytecode) {
//            for (Bug bug : turnOrder) {
//                commandsMap.putAll(bug.getCommands());
//                Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
//                System.out.println("Bug front coords: " + bugFrontCoords);
//
//                //int action = bug.determineAction(getEntityAtCoords(bugFrontCoords), script);
//                System.out.println("Current action: " + action);
//                if (!actions.containsKey(action)) {
//                    if (commandsMap.containsKey(action)) {
//                        commandsMap.get(action).run(bug);
//                    } else {
//                        throw new RuntimeException("Invalid command: " + action);
//                    }
//                }
//
//                actionsTaken.add(new ActionSummary(bug.getCoords(), action));
//                actions.get(action).run(bug);
//            }
//        }
//
//        updateGrid();
//
//        print();
//
//        return new TickSummary(actionsTaken, lastSwarmStanding());
//    }

    public TickSummary nextTick(Script script) {
        int[] bytecode = Arrays.copyOf(script.getBytecode(), script.getBytecode().length);

        // Calculate turn order
        TurnOrderCalculator turnOrderCalculator = new TurnOrderCalculator(grid, null);
        List<Bug> turnOrder = turnOrderCalculator.calculateTurnOrder();

        // Execute actions in the turn order
        List<ActionSummary> actionsTaken = new ArrayList<>();
        for (int action : bytecode) {
            for (Bug bug : turnOrder) {
                Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
                //System.out.println("Bug front coords: " + bugFrontCoords);
                //System.out.println("Current action: " + action);

                // Check if the action code is in the range 30-35
                if (action >= 30 && action <= 35) {
                    // Call the corresponding method in the Bug class based on the action code
                    switch (action) {
                        case 30:
                            bug.ifEnemy(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 31:
                            bug.ifAlly(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 32:
                            bug.ifFood(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 33:
                            bug.ifEmpty(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 34:
                            bug.ifWall(getEntityAtCoords(bugFrontCoords));
                            break;
                        case 35:
                            bug._goto(getEntityAtCoords(bugFrontCoords));
                            break;
                        default:
                            throw new RuntimeException("Invalid command: " + action);
                    }
                    commands.get(action).execute(bug);
                } else if(actions.containsKey(action)) {
                    actionsTaken.add(new ActionSummary(bug.getCoords(), action));
                    actions.get(action).run(bug);
                } else {
                    throw new RuntimeException("Invalid command: " + action);
                }
            }
            // Update the grid after each action
            updateGrid();
        }

        //print();

        return new TickSummary(actionsTaken, lastSwarmStanding());
    }

    private void updateGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof Bug bug) {
                    Point newCoords = bug.getCoords();
                    if (newCoords == null) { // Check if coordinates are null
                        // Initialize bug coordinates if they are null
                        bug.setCoords(new Point(i, j));
                        System.out.println("Bug detected: " + bug);
                        System.out.println("Initializing coordinates: " + newCoords);
                        bug.setDirection(Direction.NORTH);
                        newCoords = bug.getCoords();
                    } else {
                        // Clear the old position of the bug in the grid
                        grid[i][j] = null;
                    }
                    //System.out.println("New Coords for bug " + bug + ": " + newCoords);
                    grid[newCoords.y][newCoords.x] = bug; // Corrected coordinates assignment
                } else if (grid[i][j] instanceof Food || grid[i][j] instanceof Wall || grid[i][j] instanceof EmptySpace) {
                }
            }
        }
    }



    private void init() {
        actions.put(0, bug -> noop(bug));
        actions.put(10, bug -> mov(bug));
        actions.put(11, bug -> rotr(bug));
        actions.put(12, bug -> rotl(bug));
        actions.put(13, bug -> att(bug));
        actions.put(14, bug -> eat(bug));
    }



//List of actions would be new ArrayList(0, 10, 11, 12, 13, 14)
    //for each action, use actionMap.get(action)
    private void noop(Bug bug) {
        // do nothing!
    }
    private void mov(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity destination = getEntityAtCoords(bugFrontCoords);

        //if (destination != null) return;

        grid[bugFrontCoords.y][bugFrontCoords.x] = bug;
        grid[bug.getCoords().y][bug.getCoords().x] = null;
        bug.setCoords(bugFrontCoords);

        bug.setMoved(true); // Set moved flag to true regardless of movement success
    }


    private void rotr(Bug bug) {
        // Before rotation
        //System.out.println("Before rotr: " + bug.getDirection());

        // Perform rotation
        Direction newDirection = bug.getDirection().turnRight();
        bug.setDirection(newDirection);

        // After rotation
        //System.out.println("After rotr: " + bug.getDirection());
    }

    private void rotl(Bug bug) {
        Direction newDirection = bug.getDirection().turnLeft();
        bug.setDirection(newDirection);
    }

    private void att(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity target = getEntityAtCoords(bugFrontCoords);

        if (target == null) return;

        if (target instanceof Bug) {
            removeBugAndReplaceWithFood(bugFrontCoords);
        } else if (target instanceof Food) {
            removeFood(bugFrontCoords);
        }
    }

    private void removeBugAndReplaceWithFood(Point bugFrontCoords) {
        Entity entity = grid[bugFrontCoords.y][bugFrontCoords.x];
        if (!(entity instanceof Bug)) return;

        Bug targetBug = (Bug) entity;
        if (bugs.indexOf(targetBug) < index) index--;
        bugs.remove(targetBug);
        grid[bugFrontCoords.y][bugFrontCoords.x] = new Food();
    }

    private void removeFood(Point bugFrontCoords) {
        Entity entity = grid[bugFrontCoords.y][bugFrontCoords.x];
        if (!(entity instanceof Food)) return;

        grid[bugFrontCoords.y][bugFrontCoords.x] = null;
    }

    private void eat(Bug bug) {
        Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
        Entity target = getEntityAtCoords(bugFrontCoords);

        // Check if the target is Food
        if (!(target instanceof Food)) return;

        // Create a new instance of Bug with the same properties as the original bug
        Bug newSpawn = new Bug(
                bugFrontCoords,
                bug.getSwarm(),
                bug.getUserBytecode(),
                bug.getDirection(), // Assuming the direction remains the same
                bug.getBugType()
        );

        // Update the grid and add the new Bug instance to the list of bugs
        grid[bugFrontCoords.y][bugFrontCoords.x] = newSpawn;
        bugs.add(index, newSpawn);
        index++;

        // Update the bug's properties to reflect reproduction
        bug.setUserBytecode(doubleUserBytecode(bug.getUserBytecode()));
    }

    // Helper method to double the bytecode of a bug
    private int[] doubleUserBytecode(int[] UserBytecode) {
        int[] newUserBytecode = new int[UserBytecode.length * 2];
        System.arraycopy(UserBytecode, 0, newUserBytecode, 0, UserBytecode.length);
        System.arraycopy(UserBytecode, 0, newUserBytecode, UserBytecode.length, UserBytecode.length);
        return newUserBytecode;
    }

    private Entity getEntityAtCoords(Point coords) {
        int y = coords.y;
        int x = coords.x;

        // Check if coordinates are within the bounds of the grid
        if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
            //System.out.println("Valid Coords: " + coords);
            return grid[y][x];
        } else {
            System.out.println("Coordinates are out of bounds from getEntityAtCoords method.");
            // Coordinates are out of bounds, return null or handle appropriately
            return null;
        }
    }
    private boolean lastSwarmStanding() {
        return bugs.stream().map(Bug::getSwarm).distinct().limit(2).count() <= 1;
    }


    public void loadCommands() {
        commands.put(30, this::ifEnemy);
        commands.put(31, this::ifAlly);
        commands.put(32, this::ifFood);
        commands.put(33, this::ifEmpty);
        commands.put(34, this::ifWall);
        commands.put(35, this::_goto);
    }

    public boolean ifEnemy(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug enemyBug = (Bug) frontEntity;
            return enemyBug.getSwarm() != swarm;
        }
        return false;
    }

    public boolean ifAlly(Entity frontEntity) {
        if (frontEntity instanceof Bug) {
            Bug allyBug = (Bug) frontEntity;
            return allyBug.getSwarm() == swarm;
        }
        return false;
    }

    public boolean ifFood(Entity frontEntity) {
        return frontEntity instanceof Food;
    }

    public boolean ifEmpty(Entity frontEntity) {
        return frontEntity == null;
    }

    public boolean ifWall(Entity frontEntity) {
        return frontEntity instanceof Wall;
    }

    public boolean _goto(Entity frontEntity) {
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


    @FunctionalInterface
    interface Action {
        void run(Bug bug);
    }

    @FunctionalInterface
    interface Command {
        boolean execute(Entity frontEntity);
    }
}
