package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.service.ScriptService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    ScriptService scriptService;


}
