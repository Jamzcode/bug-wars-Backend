package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/scripts")
public class ScriptController {

    @Autowired
    ScriptService scriptService;


}
