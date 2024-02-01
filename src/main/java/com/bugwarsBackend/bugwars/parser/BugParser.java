package com.bugwarsBackend.bugwars.parser;


import java.util.*;
import java.util.stream.Collectors;

public class BugParser {
    private final Map<String, Integer> actions;
    private final Map<String, Integer> controls;
    private final Map<String, Integer> labels = new HashMap<>();
    private final List<Integer> bytecode = new ArrayList<>();
    private final Map<String, List<Integer>> labelPlaceholders = new HashMap<>();

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

        checkForMissingDestinations(labelPlaceholders);
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
        String[] tokens = removeTokens(line);

        if (tokens.length > 0) {
            if (tokens[0].equals(":")) {
                parseLabel(tokens, lineNumber);
            } else {
                processCommand(tokens, lineNumber);
            }
        }
    }

    /*
    method purpose:
    if first token = control command, call method processFlowControl
    if first token = actions command, call method processAction
 */
    private void processCommand(String[] tokens, int lineNumber) throws BugParserException {
        if (controls.containsKey(tokens[0])) {
            processFlowControl(tokens, lineNumber);
        } else if (actions.containsKey(tokens[0])) {
            processAction(tokens);
        } else {
            throw new BugParserException(String.format("Unrecognized command on line %d: %s", lineNumber, tokens[0]));
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
        if (label.isEmpty()) {
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
      method purpose: extracts, validates, and processes label in assembly code
      adds the label to a map ("labels"), checks for duplicates, and stores its position in bytecode
     */
    private void parseLabel(String[] tokens, int lineNumber) throws BugParserException {
        String label = tokens[1].substring(1);
        validateLabels(label, lineNumber);
        checkForDuplicateLabel(label, lineNumber);

        labels.put(label, bytecode.size());
        processLabelPlaceholders(label);
    }

    /*
        method purpose: checks if labelPlaceholders map has a label.
        if there is a label, find the list of locations in bytecode associated with the label.
        for each location, update bytecode of each location with current size of bytecode list.
        replaces position of label in bytecode, then removes label from map.
     */
    private void processLabelPlaceholders(String label) {
        if (labelPlaceholders.containsKey(label)) {
            labelPlaceholders.get(label).forEach(location -> bytecode.set(location, bytecode.size()));
            labelPlaceholders.remove(label);
        }
    }


    private void processFlowControl(String[] tokens, int lineNumber) throws BugParserException {
        //check if statement has correct number of tokens
        if (tokens.length != 2) {
            throw new BugParserException(String.format("Incorrect number of tokens on line %d", lineNumber));
        }
        //extract command and target from tokens
        String command = tokens[0];
        String target = tokens[1];

        //validate label names, check for duplicates
        validateLabels(target, lineNumber);

        //add bytecode representation of the control command
        bytecode.add(controls.get(command));
        //add the destination position to the bytecode
        bytecode.add(getDestination(target));
    }

    /*
        checks if label is already defined
        throws exception if it is a duplicate
     */
    private void checkForDuplicateLabel(String label, int lineNumber) throws BugParserException {
        if (labels.containsKey(label)) {
            throw new BugParserException(String.format("Duplicate label on line %d: %s", lineNumber, label));
        }
    }

    /*
        method purpose: finds destination position for given label in bytecode
        if label is found, returns its position (this is the missing ? in the array!!!)
        or null if it's a placeholder

        **I could also return specific position, but do not think I need to do this
     */
    private Integer getDestination(String target) {
        if (labels.containsKey(target)) {
            return labels.get(target);
        } else {
            processLabelPlaceholders(target);
            return null;
        }
    }

    private void processAction(String[] tokens) {
        bytecode.add(actions.get(tokens[0]));
    }

    private void checkForMissingDestinations(Map<String, List<Integer>> labelPlaceholders) throws BugParserException {
        if (labelPlaceholders.isEmpty()) {
            return; // No missing destinations
        }

        // Sort entries based on the first line number of each label
        List<Map.Entry<String, List<Integer>>> sortedEntries = new ArrayList<>(labelPlaceholders.entrySet());
        sortedEntries.sort(Comparator.comparing(entry -> entry.getValue().get(0)));

        // Build the missingLabelList string
        StringBuilder missingLabelListBuilder = new StringBuilder();
        for (Map.Entry<String, List<Integer>> entry : sortedEntries) {
            String label = entry.getKey();
            List<Integer> lineNumbers = entry.getValue();

            // Append label and associated line numbers to the StringBuilder
            missingLabelListBuilder.append(String.format("%s on line(s) [%s]", label,
                    lineNumbers.stream().map(String::valueOf).collect(Collectors.joining(", "))));

            // Append a comma if it's not the last entry
            if (sortedEntries.indexOf(entry) < sortedEntries.size() - 1) {
                missingLabelListBuilder.append(", ");
            }
        }
        // Throw the exception with the formatted missingLabelList
        throw new BugParserException("Could not find label for the following targets: " + missingLabelListBuilder);
    }

    private void fixDanglingDefinition() {

    }

    private void checkForInfiniteLoop() throws BugParserException {

    }

    private boolean hasCycle(int truCondition) {
        return false;
    }

    private int nextInstruction(int i, int trueCondition) {
        return i;
    }
}

