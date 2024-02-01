package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScriptServiceTests {

    private final User USER = new User("usernameTest", "emailTest", "passwordTest");
    private final Script SCRIPT_1 = new Script(1L, USER, "side to side", ":START rotr rotl rotl rotr", "11 12 11 12 11 12", true);
    private final Script SCRIPT_2 = new Script(2L, USER, "move attack", ":START mov att", "10 13 10 13 10 13", true);
    private final Script SCRIPT_3 = new Script(3L, USER, "move in line", ":START mov", "10 10 10 10 10 10 10", true);

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScriptRepository scriptRepository;

    @InjectMocks
    private ScriptService scriptService;

    @Test
    public void getAllValidScripts_returnsAllValidScripts() {

    }

    @Test
    public void getUserScripts_returnsUserScripts() {

    }

    @Test
    public void getScriptById_returnsCorrectScript() {

    }

    @Test
    public void getScriptById_throwsExceptionIfScriptDoesNotExist() {

    }

    @Test
    public void getScriptById_throwsExceptionIfForbidden() {

    }

    @Test
    public void createScript_returnsCreatedScript() {

    }

    @Test
    public void createScript_throwsExceptionIfScriptNameAlreadyExists() {

    }

    @Test
    public void updateScript_returnsUpdatedScript() {

    }

    @Test
    public void updateScript_throwsExceptionIfScriptDoesNotExist() {

    }

    @Test
    public void updateScript_throwsExceptionIfForbidden() {

    }

    @Test
    public void deleteScriptById_deletesScript() {

    }

    @Test
    public void deleteScriptById_throwsExceptionIfScriptDoesNotExist() {

    }

    @Test
    public void deleteScriptById_throwsExceptionIfForbidden() {

    }

}
