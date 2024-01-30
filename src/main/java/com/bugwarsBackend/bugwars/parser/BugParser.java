package com.bugwarsBackend.bugwars.parser;


import java.io.File;
import java.io.FileNotFoundException;
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
            //parseLine(line);
        }
        return bytecode;
    }

    /*
        parses each line,
        identifies each type of command - label, commands, control & processes it
        if the first token is a control, then it should have a label
        if the first token in this array is an action, then that should be only thing on the line
     */
    public void parseLine(String line) throws BugParserException {
        removeComments(line);
        String [] tokens = removeTokens(line);

        if(tokens.length > 0) {
            if (tokens[0].equals(":")) {
                parseLabel(tokens);
            } else {
                processCommand(tokens);
            }
        }
    }

    /*
        check if this label has
     */
    public void validateLabels(String label) throws BugParserException {
        if(label.isEmpty()) {
            throw new BugParserException("Empty label found");
        }
    }

    /*
        must remove tokens (which are commands, conditionals)
        from bug assembly code
        and remove comments
     */
    public String[] removeTokens(String line) {
        return new String[0];
    }

    /*
      finds line that starts with label (":")
      validates the label, checks for duplicate labels,
      handles label placeholders
     */
    public void parseLabel(String [] tokens) {
    }

    /*
    remove possible # (comments) user might input
 */
    public void removeComments(String line) {

    }

    /*
        checks if label is already defined
        throws exception if it is a duplicate
     */
    public void checkForDuplicateLabel(String label) throws BugParserException {

    }

    /*
        finds the missing position/destination associated with label in bytecode
        use floyd's algorithm (tortoise and hare)
     */
    public Integer getMissingPosition(String target) {

        return null;
    }

    /*
        add logic to process other commands here
        for ex., convert commands to bytecode
     */
    public void processCommand(String[] tokens) {

    }
}
