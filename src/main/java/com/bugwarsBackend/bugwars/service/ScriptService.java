package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.ScriptRequest;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService {
    @Autowired
   ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;

    public List<Script> getAllScripts() {
        return scriptRepository.isBytecodeValid();



    }

    public Script getScript(Long id, Principal principal) {
        User user = getUser(principal);

        Optional<Script> scriptOptional = scriptRepository.findById(id);

        if(scriptOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Script does not exist");
        } else if (!user.getId().equals(scriptOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must be the owner of this script to access it");
        } else {
            return scriptOptional.get();
        }

    }

    public List<Script> getUserScripts(Principal principal) {
        User user = getUser(principal);

        return scriptRepository.getScriptsByUser(user);
    }

    public Script createScript(ScriptRequest request, Principal principal) {
        Script script = new Script();
        User user = getUser(principal);

        if (scriptRepository.scriptNameExists(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Script name already exists");
        }

        //add script fields here

    }

    public Script updateScript() {

    }

    public void deleteScriptById(Long id, Principal principal) {
        User user = getUser(principal);
        Optional<Script> scriptOptional = scriptRepository.findById(id);

        if (scriptOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Script does not exist");
        }

        if (!user.getId().equals(scriptOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to delete this script. You must be the owner of this script to delete it");
        }

        scriptRepository.deleteById(id);
    }


    private User getUser(Principal principal) {
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User does not exist.");
        }
    }




}
