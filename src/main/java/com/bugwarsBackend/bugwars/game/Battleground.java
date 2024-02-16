package com.bugwarsBackend.bugwars.game;

import com.bugwarsBackend.bugwars.game.entity.*;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Battleground {
    private final Entity[][] grid;


    public Battleground(Resource mapResource) {
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
            case '0':
                return new Bug(0);
            case '1':
                return new Bug(1);
            case 'F':
                return new Food();
            case ' ':
                return new EmptySpace();
            default:
                throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }
    }

    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }

}
