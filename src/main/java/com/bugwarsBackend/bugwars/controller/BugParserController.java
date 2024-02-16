package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.dto.request.BugParserRequest;
import com.bugwarsBackend.bugwars.service.BugParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/parse")
public class BugParserController {
    @Autowired
    BugParserService bugParserService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> parse(@RequestBody BugParserRequest bugParserRequest) {
        return bugParserService.parse(bugParserRequest);
    }
}
