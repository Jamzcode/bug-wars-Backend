package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.dto.request.ScriptRequest;
import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.service.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/scripts")
public class ScriptController {

    @Autowired
    ScriptService scriptService;

    @GetMapping("/all")
    public List<ScriptName> getAllValidScripts() {
        return scriptService.getAllValidScripts();
    }

    @GetMapping
    public List<Script> getUserScripts(Principal principal){
        return scriptService.getUserScripts(principal);
    }

    @GetMapping(path = "/{id}")
    public Script getScriptById(@PathVariable Long id, Principal principal) {
        return scriptService.getScriptById(id, principal);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Script createScript(@RequestBody ScriptRequest request, Principal principal) {
        return scriptService.createScript(request, principal);
    }

    @PutMapping(path = "/{id}")
    public Script updateScript(@PathVariable Long id, Principal principal,
                               @RequestBody ScriptRequest request) {
        return scriptService.updateScript(id, request, principal);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteScriptById(@PathVariable Long id, Principal principal) {
        scriptService.deleteScriptById(id, principal);
    }
}
