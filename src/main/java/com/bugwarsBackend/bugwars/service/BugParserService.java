package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.BugParserRequest;
import com.bugwarsBackend.bugwars.parser.BugParser;
import com.bugwarsBackend.bugwars.parser.BugParserException;
import com.bugwarsBackend.bugwars.parser.BugParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BugParserService {

    private final BugParserFactory bugParserFactory;

    @Autowired
    public BugParserService(BugParserFactory bugParserFactory) {
        this.bugParserFactory = bugParserFactory;
    }

    public List<Integer> parse(BugParserRequest bugParserRequest) {
        try {
            BugParser parser = bugParserFactory.createInstance();
            return parser.parse(bugParserRequest.getCode());
        } catch (BugParserException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
