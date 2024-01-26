package com.bugwarsBackend.bugwars.parser;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BugParser {
    private final Map<String, Integer> actions;
    private final Map<String, Integer> controls;

    public BugParser(Map<String, Integer> actions, Map<String, Integer> controls) {
        this.actions = actions;
        this.controls = controls;
    }

    //fix this method, don't need a scanner, just use a for loop to search through user input
    //then split up the spaces
    public List<Integer> parseFile(String filePath) throws BugParserException {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            StringBuilder codeBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                codeBuilder.append(scanner.nextLine().append(System.lineSeparator()))
            }
            return parse(codeBuilder.toString());
        } catch(FileNotFoundException e) {
            throw new BugParserException("File not found.", e);
        }
    }
}
