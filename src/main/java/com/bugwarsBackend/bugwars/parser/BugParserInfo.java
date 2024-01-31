package com.bugwarsBackend.bugwars.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BugParserInfo {
    private final Map<String, Integer> labelLineNumbers = new HashMap<>();
    // map to associate labels with list of line numbers
    private final Map<String, List<Integer>> labelPlaceHolderLineNumbers = new HashMap<>();
    private String line;
    private int lineNumber;
    private int charPosition;

    public void setLineInfo(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public Integer getLabelLineNumber(String label) {
        return this.labelLineNumbers.get(label);
    }

    public void addLabelLineNumber(String label) {
        this.labelLineNumbers.put(label, this.lineNumber);
    }

    public List<Integer> getLabelPlaceholderLineNumbers(String label) {
        return labelPlaceHolderLineNumbers.get(label);
    }

    //  if a key ('label') is present, returns existing value associated with key. then add lineNumber to list.
    public void addLabelPlaceholderLineNumber(String label) {
        labelPlaceHolderLineNumbers.computeIfAbsent(label, k -> new ArrayList<>()).add(this.lineNumber);
    }
}
