package com.bugwarsBackend.bugwars.parser;


import com.bugwarsBackend.bugwars.config.BugAssemblyCommands;

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
        this.controls = convertKeysToLowerCase(controls);;
    }

    public BugParser() {
        this.actions = new HashMap<>();
        this.controls = new HashMap<>();
    }

    /*
    given assembly code from user (userInput), processes each line,
    returns list of integers representing bytecode;
    checks for missing position ? in code. for ex. in [10, 25, ?, 4] <-- ? is missing label (helper method)
    checks for empty labels (helper method)
    checks for infinite loops (helper method)
     */
    public List<Integer> parse(String userInput) throws BugParserException {
        //Convert to lowercase
        userInput = userInput.toLowerCase();

        String[] lines = userInput.split("\\R");
        for (int i = 0; i < lines.length; i++) {
            parseLine(lines[i], i + 1);
        }

        ensureValidLabelPosition();
        checkForMissingDestinations(labelPlaceholders);
        checkForInfiniteLoop();
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

//    private void parseLine(String line, int lineNumber) throws BugParserException {
//        // Remove comments from the line
//        line = removeComments(line);
//
//        // Split the line into tokens
//        String[] tokens = removeTokens(line);
//
//        // Iterate over each token
//        for (String token : tokens) {
//            if (token.contains(":")) {
//                parseLabel(tokens, lineNumber);
//            } else {
//                // If the token is not a label, process it as a regular command
//                processCommand(token, lineNumber);
//            }
//        }
//    }

    private void parseLine(String line, int lineNumber) throws BugParserException {
        // Remove comments from the line and convert to lowercase
        line = removeComments(line);

        // Split the line into tokens
        String[] tokens = removeTokens(line);

        // Iterate over each token
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].toLowerCase();
            if (token.contains(":")) {
                String label = token.substring(1); // Remove the ":" from the label
                Integer controlBytecode = controls.get(label); // Check if the label matches a control command
                if (controlBytecode != null) {
                    bytecode.add(controlBytecode); // Add the bytecode value to the list
                } else {
                    processAction(token);
                }
//                parseLabel(tokens[i], lineNumber);
//                String target = tokens[i+1]; // Get the target label
//                processFlowControl(token, target, lineNumber);
//                i++; // Skip the next token since it's the target label
            } else {
                processAction(token);
            }
        }
    }



//    private void parseLine(String line, int lineNumber) throws BugParserException {
//        // Remove comments from the line
//        line = removeComments(line);
//
//        // Split the line into tokens
//        String[] tokens = removeTokens(line);
//
//        // Iterate over each token
//        for (int i = 0; i < tokens.length; i++) {
//            String token = tokens[i];
//            if (token.contains(":")) {
//                // Check if i+1 is within the bounds of the tokens array
//                if (i + 1 < tokens.length) {
//                    // Call parseLabel with the next token as the label
//                    parseLabel(tokens[i+1], lineNumber);
//                    String target = tokens[i+1]; // Get the target label
//                    processCommand(token, target, lineNumber);
//                    i++; // Skip the next token since it's the target label
//                } else {
//                    // If i+1 is out of bounds, throw an exception or handle the situation accordingly
//                    throw new BugParserException("Label is missing");
//                }
//            } else {
//                processAction(token);
//            }
//        }
//    }

    /*
    method purpose:
    if first token = control command, call method processFlowControl
    if first token = actions command, call method processAction
 */
//    private void processCommand(String[] tokens, int lineNumber) throws BugParserException {
//        if (controls.containsKey(tokens[0])) {
//            processFlowControl(tokens, lineNumber);
//        } else if (actions.containsKey(tokens[0])) {
//            processAction(tokens);
//        } else {
//            throw new BugParserException(String.format("Unrecognized command on line %d: %s", lineNumber, tokens[0]));
//        }
//    }

    private void processCommand(String token, String target, int lineNumber) throws BugParserException {
        if (controls.containsKey(token)) {
            processFlowControl(token, target, lineNumber);
//        } else if (actions.containsKey(token)) {
//            processAction(token);
        } else {
            throw new BugParserException(String.format("Unrecognized command on line %d: %s", lineNumber, token));
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
        int index = line.indexOf("#");
        if (index == -1) {
            return line;
        }

        return line.substring(0, index);
    }

    /*
      method purpose: extracts, validates, and processes label in assembly code
      adds the label to a map ("labels"), checks for duplicates, and stores its position in bytecode
     */
    private void parseLabel(String token, int lineNumber) throws BugParserException {
        String label = token;
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


//    private void processFlowControl(String tokens, int lineNumber) throws BugParserException {
//        //check if statement has correct number of tokens
//        if (tokens.length != 2) {
//            throw new BugParserException(String.format("Incorrect number of tokens on line %d", lineNumber));
//        }
//        //extract command and target from tokens
//        String command = tokens[0];
//        String target = tokens[1];
//
//        //validate label names, check for duplicates
//        validateLabels(target, lineNumber);
//
//        //add bytecode representation of the control command
//        bytecode.add(controls.get(command));
//        //add the destination position to the bytecode
//        bytecode.add(getDestination(target));
//    }

    private void processFlowControl(String token, String target, int lineNumber) throws BugParserException {
        // Validate the target label
        validateLabels(target, lineNumber);

        // Add the bytecode representation of the control command
        bytecode.add(controls.get(token));

        // Add the destination position to the bytecode
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

    private void processAction(String tokens) {
        bytecode.add(actions.get(tokens));
    }

    /*
        method purpose:
        missingLabelList is a string that lists all missing labels
        iterator iterates through labelPlaceholders map and appends missing labels to it
        for each entry, it retrieves the label ("key") associated with lineNumbers ("value")
        it appends the label and associated line numbers to the missingLabelList

     */
    private void checkForMissingDestinations(Map<String, List<Integer>> labelPlaceholders) throws BugParserException {
        if (labelPlaceholders.isEmpty()) {
            return; // no missing destinations
        }

        // build the missingLabelList string without sorting
        StringBuilder missingLabelListBuilder = new StringBuilder();
        Iterator<Map.Entry<String, List<Integer>>> iterator = labelPlaceholders.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<Integer>> entry = iterator.next();
            String label = entry.getKey();
            List<Integer> lineNumbers = entry.getValue();

            // append label and associated line numbers to the StringBuilder
            missingLabelListBuilder.append(String.format("%s on line(s) [%s]", label,
                    lineNumbers.stream().map(String::valueOf).collect(Collectors.joining(", "))));

            // append a comma if it's not the last entry
            if (iterator.hasNext()) {
                missingLabelListBuilder.append(", ");
            }
        }
        // throw the exception with the formatted missingLabelList
        throw new BugParserException("Could not find label for the following targets: " + missingLabelListBuilder);
    }


    /*
        method purpose: checks if there is a second-to-last command in the bytecode that is
        a control command and if last command's position is out of bounds.
        if so, it sets the last command in the bytecode to 0.
        ** assume the last position is invalid/dangling.
     */
//    private void ensureValidLabelPosition() {
//        int size = bytecode.size();
//
//        if (size >= 2) {
//            int penultimateCommand = bytecode.get(size - 2);
//            int ultimateCommand = bytecode.get(size - 1);
//
//            if (controls.containsValue(penultimateCommand) && ultimateCommand >= size) {
//                bytecode.set(size - 1, 0);
//            }
//        }
//    }
    private void ensureValidLabelPosition() {
        int size = bytecode.size();

        if (size >= 2) {
            Integer penultimateCommand = bytecode.get(size - 2);  // Use Integer instead of int to handle null values
            Integer ultimateCommand = bytecode.get(size - 1);

            // Check if penultimateCommand or ultimateCommand is null before accessing intValue()
            if (penultimateCommand != null && ultimateCommand != null && controls.containsValue(penultimateCommand) && ultimateCommand.intValue() >= size) {
                bytecode.set(size - 1, 0);
            }
        }
    }


    /*
        method purpose: checks if there is an infinite loop
     */
    private void checkForInfiniteLoop() throws BugParserException {
        for (int trueCondition : controls.values()) {
            if (hasCycle(trueCondition)) {
                throw new BugParserException("Infinite loop detected");
            }
        }
    }

    /*
        method purpose: checks if there is an infinite cycle in the bytecode
     */
    private boolean hasCycle(int trueCondition) {
        Set<Integer> visited = new HashSet<>();
        int currentCommand = trueCondition;

        while (currentCommand != -1) {
            if (!visited.add(currentCommand)) {
                return true;  // Detected a cycle
            }

            // Update current command based on control flow
            currentCommand = getNextCommand(currentCommand);
        }

        return false;  // No cycle detected
    }

    /*
        method purpose: returns the next command based on control flow
     */
//    private int getNextCommand(int currentCommand) {
//        // find the index of the current command in the bytecode
//        int i = bytecode.indexOf(currentCommand);
//
//        // check if the current command is in the bytecode
//        if (i != -1) {
//            // get the values associated with control flow conditions
//            int trueCondition = controls.get("trueCondition");
//            int gotoCommand = controls.get("goto");
//
//            // check if the current command is a trueCondition or goto command
//            if (List.of(trueCondition, gotoCommand).contains(currentCommand)) {
//                // return the command following the current one in the bytecode
//                return bytecode.get(i + 1);
//            } else {
//                // return the command two positions ahead (modulo to handle wrapping)
//                return (i + 2) % bytecode.size();
//            }
//        }
//
//        // default case: return -1 indicating end of control flow
//        return -1;
//    }

    private int getNextCommand(int currentCommand) {
        // find the index of the current command in the bytecode
        int i = bytecode.indexOf(currentCommand);

        // check if the current command is in the bytecode
        if (i != -1) {
            // get the values associated with control flow conditions if they exist
            Integer trueCondition = controls.get("trueCondition");
            Integer gotoCommand = controls.get("goto");

            // check if the current command is a trueCondition or goto command
            if (trueCondition != null && gotoCommand != null && List.of(trueCondition.intValue(), gotoCommand.intValue()).contains(currentCommand)) {
                // return the command following the current one in the bytecode
                return bytecode.get(i + 1);
            } else {
                // return the command two positions ahead (modulo to handle wrapping)
                return (i + 2) % bytecode.size();
            }
        }

        // default case: return -1 indicating end of control flow
        return -1;
    }



    /*
        method purpose: returns the control by value;
     */
    private String getControlByValue(int trueCondition) {
        // iterate through the controls map
        for (Map.Entry<String, Integer> entry : controls.entrySet()) {
            // check if the value associated with the current entry matches the provided trueCondition
            if (entry.getValue() == trueCondition) {
                // return the key (control) associated with the matching value
                return entry.getKey();
            }
        }

        // if no match is found, throw an exception indicating that the condition is not in the map
        throw new IllegalArgumentException("Condition not in map.");
    }

    private Map<String, Integer> convertKeysToLowerCase(Map<String, Integer> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(), Map.Entry::getValue));
    }
}

