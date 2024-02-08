package com.bugwarsBackend.bugwars.parser;

import com.bugwarsBackend.bugwars.config.BugAssemblyCommands;
import org.springframework.stereotype.Component;

@Component
public class BugParserFactory {
    public BugParser createInstance() {
        return new BugParser(BugAssemblyCommands.getActions(),
                BugAssemblyCommands.getControls());
    }
}
