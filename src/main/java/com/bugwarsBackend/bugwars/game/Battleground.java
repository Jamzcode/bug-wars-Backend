package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.Bug;
import com.bugwarsBackend.bugwars.game.entity.Entity;
import com.bugwarsBackend.bugwars.game.entity.Food;
import lombok.Data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Battleground {
    private final List<Bug> bugs;
    private final String name;
    private final Entity[][] grid;
    private final Map<Integer, Action> actions = new HashMap<>();
    private int index;

    public Battleground(String name, Entity[][] grid, List<Bug> bugs) {
        this.name = name;
        this.grid = grid;
        this.bugs = bugs;

        init();
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

        // Check if the target is either a Bug or Food
        if (target instanceof Bug) {
            // If the target is a Bug, remove it from the list of bugs and replace it with Food
            Bug targetBug = (Bug) target;
            if (bugs.indexOf(targetBug) < index) index--;
            bugs.remove(targetBug);
            grid[bugFrontCoords.y][bugFrontCoords.x] = new Food();
        } else if (target instanceof Food) {
            // If the target is Food, remove it from the grid
            grid[bugFrontCoords.y][bugFrontCoords.x] = null;
        }
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
                bug.getDirection() // Assuming the direction remains the same
        );

        // Update the grid and add the new Bug instance to the list of bugs
        grid[bugFrontCoords.y][bugFrontCoords.x] = newSpawn;
        bugs.add(index, newSpawn);
        index++;
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

    private record ActionSummary(Point coordinates, int action) {
    }
}
