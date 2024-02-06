package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.parser.BugParser;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ScriptServiceTests {

    private final User USER = new User("usernameTest", "emailTest", "passwordTest");
    private final Script SCRIPT_1 = new Script(1L, USER, "side to side", ":START rotr rotl rotl rotr", "11 12 11 12 11 12", true);
    private final Script SCRIPT_2 = new Script(2L, USER, "move attack", ":START mov att", "10 13 10 13 10 13", true);
    private final Script SCRIPT_3 = new Script(3L, USER, "move and right", ":START mov rotr", "10 10 10 10 10 10 10 11", false);
    private final Script SCRIPT_4 = new Script(4L, USER, "move in line", ":START mov", "10 10 10 10 10 10 10", true);

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScriptRepository scriptRepository;

    @Mock
    private BugParser bugParser;

    @InjectMocks
    private ScriptService scriptService;

    @Test
    public void getAllValidScripts_returnsAllValidScripts() {
        // Set up
        when(scriptRepository.getAllValidScripts()).thenReturn(List.of(SCRIPT_1, SCRIPT_2, SCRIPT_4));

        // Execute
        List<ScriptName> result = scriptService.getAllValidScripts();

        // Verify
        Assertions.assertThat(result).hasSize(3);

    }

    @Test
    public void getUserScripts_returnsUserScripts() {
        // Set up
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("usernameTest");
        when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.of(USER));
        when(scriptRepository.getScriptsByUser(USER)).thenReturn(List.of(SCRIPT_1, SCRIPT_2, SCRIPT_3, SCRIPT_4));

        // Execute
        List<Script> result = scriptService.getUserScripts(principal);

        // Verify
        Assertions.assertThat(result).containsExactly(SCRIPT_1, SCRIPT_2, SCRIPT_3, SCRIPT_4);
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
