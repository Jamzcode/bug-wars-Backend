package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.Bug;
import com.bugwarsBackend.bugwars.game.entity.Entity;
import com.bugwarsBackend.bugwars.game.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Battleground {
    private List<Bug> bugs;
    private String name;
    private Entity[][] grid;
    private final Map<Integer, Action> actions = new HashMap<>();

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
    }

    public void print() {
        for (Entity[] entities : grid) {
            for (Entity e : entities) {
                if (e == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(e);
                }
            }
            System.out.println();
        }
    }

    public TickSummary nextTick() {
        List<ActionSummary> actionsTaken = new ArrayList<>();
        for (Bug bug : bugs) {
            Point bugFrontCoords = bug.getDirection().goForward(bug.getCoords());
            int action = bug.determineAction(getEntityAtCoords(bugFrontCoords));
            if (!actions.containsKey(action)) throw new RuntimeException("Invalid action: " + action);

            actionsTaken.add(new ActionSummary(bug.getCoords(), action));
            actions.get(action).run(bug);
        }
        //To-do: Update grid
        return new TickSummary(actionsTaken, lastSwarmStanding());
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
        if (destination != null) return;

        grid[bugFrontCoords.y][bugFrontCoords.x] = bug;
        grid[bug.getCoords().y][bug.getCoords().x] = null;
        bug.setCoords(bugFrontCoords);
    }
    private void rotr(Bug bug) {
        bug.setDirection(bug.getDirection().turnRight());
    }

    private void rotl(Bug bug) {
        bug.setDirection(bug.getDirection().turnLeft());
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
                bug.getBytecode(),
                bug.getDirection(), // Assuming the direction remains the same
                bug.getBugType()
        );

        // Update the grid and add the new Bug instance to the list of bugs
        grid[bugFrontCoords.y][bugFrontCoords.x] = newSpawn;
        bugs.add(index, newSpawn);
        index++;

        // Update the bug's properties to reflect reproduction
        bug.setBytecode(doubleBytecode(bug.getBytecode()));
    }

    // Helper method to double the bytecode of a bug
    private int[] doubleBytecode(int[] bytecode) {
        int[] newBytecode = new int[bytecode.length * 2];
        System.arraycopy(bytecode, 0, newBytecode, 0, bytecode.length);
        System.arraycopy(bytecode, 0, newBytecode, bytecode.length, bytecode.length);
        return newBytecode;
    }

    private Entity getEntityAtCoords(Point coords) {
        return grid[coords.y][coords.x];
    }

    private boolean lastSwarmStanding() {
        return bugs.stream().map(Bug::getSwarm).distinct().limit(2).count() <= 1;
    }
    @FunctionalInterface
    interface Action {
        void run(Bug bug);
    }
}
