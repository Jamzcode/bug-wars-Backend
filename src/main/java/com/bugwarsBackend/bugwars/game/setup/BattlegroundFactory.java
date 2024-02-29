package com.bugwarsBackend.bugwars.game.setup;

import com.bugwarsBackend.bugwars.game.Battleground;
import com.bugwarsBackend.bugwars.game.entity.*;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BattlegroundFactory {
    private final Entity[][] grid;


    public BattlegroundFactory(Resource mapResource) {
        this.grid = loadMap(mapResource);
    }

    private Entity[][] loadMap(Resource mapResource) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mapResource.getInputStream(), StandardCharsets.UTF_8));

            // Read the first line to get map size and name
            String firstLine = reader.readLine();
            String[] mapInfo = firstLine.split(",");
            int mapWidth = Integer.parseInt(mapInfo[0]);
            int mapHeight = Integer.parseInt(mapInfo[1]);
            String mapName = mapInfo[2];

            System.out.println("Map Name: " + mapName);

            // Initialize the grid based on map size
            Entity[][] grid = new Entity[mapHeight][mapWidth];

            // Read the rest of the map and populate the grid
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char symbol = line.charAt(col);
                    Entity entity = createEntityFromSymbol(symbol);
                    grid[row][col] = entity;
                }
                row++;
            }

            reader.close();
            return grid;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map from resource: " + mapResource.getFilename(), e);
        }
    }


    // Can do this for front end??:
    //    const imageMap = {
    //        '0': 'url_to_image_for_0.jpg',
    //        '1': 'url_to_image_for_1.jpg'
    //    };
    private Entity createEntityFromSymbol(char symbol) {
        switch (symbol) {
            case 'X':
                return new Wall();
            case 'F':
                return new Food();
            case ' ':
                return new EmptySpace();
            case '0':
            case '1':
                int swarm = Character.getNumericValue(symbol); // Convert character to integer
                return new Bug(swarm);
            default:
                throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }
    }

    public Battleground printGrid() {
        Battleground battleground = new Battleground("Map", grid, null); // Assuming the last argument should be null
        for (int i = 0; i < battleground.getGrid().length; i++) {
            for (int j = 0; j < battleground.getGrid()[i].length; j++) {
                System.out.print(battleground.getGrid()[i][j] + " ");
            }
            System.out.println();
        }
        return battleground;
    }
}

