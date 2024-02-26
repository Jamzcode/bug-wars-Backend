package com.bugwarsBackend.bugwars.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TickSummary {
    List<ActionSummary> summary;
    boolean lastSwarmStanding;

    public boolean isGameOver() {
        // Check if there is only one swarm remaining on the battleground
        int remainingSwarms = calculateRemainingSwarms();
        return remainingSwarms == 1;
    }

    private int calculateRemainingSwarms() {
        Set<Integer> swarms = new HashSet<>();
        for (ActionSummary actionSummary : summary) {
            swarms.add(actionSummary.getSwarm()); // Access swarm directly from ActionSummary
        }
        return swarms.size();
    }

}
