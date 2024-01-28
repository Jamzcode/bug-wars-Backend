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

        }
        return bytecode;
    }

    /*
        parses each line,
        identifies each type of command - label, commands, control & processes it
     */
    public void parseLine(String line) throws BugParserException {

    }

    public void validateLabels(String label) throws BugParserException {

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
}
