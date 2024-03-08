package com.bugwarsBackend.bugwars.game.setup;

import com.bugwarsBackend.bugwars.game.Swarm;
import com.bugwarsBackend.bugwars.game.entity.Bug;
import com.bugwarsBackend.bugwars.game.entity.Entity;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TurnOrderCalculator {
    private final Entity[][] grid;
    private final List<Swarm> swarms;
    private Point coords;

    public TurnOrderCalculator(Entity[][] grid, List<Swarm> swarms) {
        this.grid = grid;
        this.swarms = swarms;
    }

    public List<Bug> calculateTurnOrder() {
        Map<Double, List<Bug>> distanceBuckets = groupBugsByDistance();
        List<Bug> roughOrder = findPairs(distanceBuckets);
        return roughOrder;
        //return sortTurnOrder(roughOrder);
    }

    private Map<Double, List<Bug>> groupBugsByDistance() {
        Map<Double, List<Bug>> distanceBuckets = new HashMap<>();
        for (Entity[] entities : grid) {
            for (Entity e : entities) {
                if (e instanceof Bug) {
                    Bug bug = (Bug) e;
                    if (bug.getCoords() != null) {
                        double distance = calculateDistance(bug.getCoords());
                        distanceBuckets.computeIfAbsent(distance, k -> new ArrayList<>()).add(bug);
                    } else {
                        System.out.println("Bug coordinates are: " + bug.getCoords());
                    }
                }
            }
        }
        return distanceBuckets;
    }


    private List<Bug> findPairs(Map<Double, List<Bug>> distanceBuckets) {
        List<Bug> roughOrder = new ArrayList<>();

        for (List<Bug> bugs : distanceBuckets.values()) {
            if (bugs.size() == 2) {
                roughOrder.addAll(bugs);
            } else {
                for (int i = 0; i < bugs.size(); i++) {
                    Bug bug1 = bugs.get(i);
                    if (!roughOrder.contains(bug1)) {
                        for (int j = i + 1; j < bugs.size(); j++) {
                            Bug bug2 = bugs.get(j);
                            if (!roughOrder.contains(bug2) && areAligned(bug1, bug2)) {
                                roughOrder.add(bug1);
                                System.out.println("Added bug1: " + bug1);
                                roughOrder.add(bug2);
                                System.out.println("Added bug2: " + bug2);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return roughOrder;
    }

    private List<Bug> sortTurnOrder(List<Bug> roughOrder) {
        roughOrder.sort(Comparator.comparingInt(Bug::getSwarm));
        return roughOrder;
    }

    private double calculateDistance(Point coords) {
        double centerX = (grid[0].length - 1) / 2.0;
        double centerY = (grid.length - 1) / 2.0;
        return Math.sqrt(Math.pow(coords.x - centerX, 2) + Math.pow(coords.y - centerY, 2));
    }

    private boolean areAligned(Bug bug1, Bug bug2) {
        boolean aligned = bug1.getCoords().x == bug2.getCoords().x || bug1.getCoords().y == bug2.getCoords().y;
        return aligned;
    }
}
