package com.bugwarsBackend.bugwars.parser;


import java.util.*;

public class BugParser {
    private final Map<String, Integer> actions;
    private final Map<String, Integer> controls;
    private final Map<String, Integer> labels = new HashMap<>();
    private final List<Integer> bytecode = new ArrayList<>();
    private final BugParserInfo info = new BugParserInfo();

    public BugParser(Map<String, Integer> actions, Map<String, Integer> controls) {
        this.actions = actions;
        this.controls = controls;
    }
    /*
    given assembly code from user (userInput), processes each line,
    returns list of integers representing bytecode;
    checks for missing position ? in code. for ex. in [10, 25, ?, 4] <-- ? is missing label (helper method)
    checks for empty labels (helper method)
    checks for infinite loops (helper method)
     */
    public List<Integer> parse(String userInput) throws BugParserException {
        String[] lines = userInput.split("\\R");
        for (int i = 0; i < lines.length; i++) {
            parseLine(lines[i], i + 1);
        }

//        checkForMissingDestinations();
//        fixDanglingLabelDefinition();
//        checkforInfiniteLoop();
        return bytecode;
    }

    /*
        method purpose: process line of Bug assembly code
        checks if there is a comment, then removes comment
        splits line into an array of tokens
        if token has a ":", then it is a LABEL
        --> if true, call parseLabel method to handle label parsing
        --> if false, assumes line is a regular command, calls processCommand to handle command parsing
     */
    private void parseLine(String line, int lineNumber) throws BugParserException {
        removeComments(line);
        String [] tokens = removeTokens(line);

        if(tokens.length > 0) {
            if (tokens[0].equals(":")) {
                parseLabel(tokens, lineNumber);
            } else {
                processCommand(tokens);
            }
        }
    }

    /*
        method iterates through each character in 'label' string
        for each character, checks if it's a valid label character:
            1. char is uppercase (A-Z)
            2. char is a digit (0-9)
            3. char is an underscore (_)
     */
    private void validateLabels(String label, int lineNumber) throws BugParserException {
        for (int i = 0; i < label.length(); i++) {
            char c = label.charAt(i);
            if (!Character.isUpperCase(c) && !Character.isDigit(c) && c != '_') {
                throw new BugParserException(String.format("Invalid label name on line %d: %s", lineNumber, label));
            }
        }
        if(label.isEmpty()) {
            throw new BugParserException("Empty label found");
        }
    }

    /*
        method splits line into array of tokens by using whitespace as the delimiter
        each element of array represents separate token extracted from original line
        "\\s+" --> matches whitespace characters like spaces or tabs
     */
    private String[] removeTokens(String line) {
        return line.split("\\s+");
    }

    // not sure if this will remove # from the right spot?
    // this removes # if it's found at index 0
    private String removeComments(String line) {
        return line.split("#")[0].trim();
    }

    /*
      finds line that starts with label (":")
      validates the label, checks for duplicate labels,
      handles label placeholders
     */
    private void parseLabel(String [] tokens, int lineNumber) {
    }

    /*
        checks if label is already defined
        throws exception if it is a duplicate
     */
    private void checkForDuplicateLabel(String label) throws BugParserException {

    }

    /*
        finds the missing position/destination associated with label in bytecode
        use floyd's algorithm (tortoise and hare)
     */
    private Integer getMissingPosition(String target) {

        return null;
    }

    /*
        add logic to process other commands here
        for ex., convert commands to bytecode
     */
    private void processCommand(String[] tokens) {

    }
}
