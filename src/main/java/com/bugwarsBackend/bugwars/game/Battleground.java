package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.Bug;
import com.bugwarsBackend.bugwars.game.entity.Entity;
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
        // Move logic
    }

    private void rotr(Bug bug) {
        // Rotate right logic
    }

    private void rotl(Bug bug) {
        // Rotate left logic
    }

    private void att(Bug bug) {
        // Attack logic
    }

    private void eat(Bug bug) {
        // Eat logic
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
