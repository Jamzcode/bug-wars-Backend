package com.bugwarsBackend.bugwars.parser;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BugParserInfo {
    private final Map<String, Integer> labelLineNumbers = new HashMap<>();
    private String line;
    private int lineNumber;
    private int charPosition;
}
