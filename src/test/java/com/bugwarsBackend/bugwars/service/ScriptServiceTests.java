package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.parser.BugParser;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        Principal principal = mockPrincipal("usernameTest");
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
        // Arrange
        Long scriptId = 1L;
        USER.setId(1L);
        Principal principal = mockPrincipal("usernameTest");
        when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.of(USER));
        when(scriptRepository.findById(scriptId)).thenReturn(Optional.of(SCRIPT_1));

        // Act
        Script result = scriptService.getScriptById(scriptId, principal);

        // Assert
        assertEquals("side to side", result.getName());
    }


    @Test
    public void getScriptById_throwsExceptionIfScriptDoesNotExist() {
        // Arrange
        Long scriptId = 1L;
        USER.setId(1L);
        Principal principal = mockPrincipal("usernameTest");
        when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.of(USER));
        when(scriptRepository.findById(scriptId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> scriptService.getScriptById(scriptId, principal))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    assertThat(exception).hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
                    assertThat(exception).hasMessageContaining("Script does not exist");
                });
    }

    @Test
    public void getScriptById_throwsExceptionIfForbidden() {
        // Arrange
        Long scriptId = 1L;
        USER.setId(1L);
        User mockUser = new User();
        mockUser.setId(2L);
        Principal principal = mockPrincipal("usernameTest");
        when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.of(mockUser));
        when(scriptRepository.findById(scriptId)).thenReturn(Optional.of(SCRIPT_1));

        // Act & Assert
        assertThatThrownBy(() -> scriptService.getScriptById(scriptId, principal))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> {
                    assertThat(exception).hasFieldOrPropertyWithValue("status", HttpStatus.FORBIDDEN);
                    assertThat(exception).hasMessageContaining("You must be the owner of this script to access it");
                });
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

    private Principal mockPrincipal(String username) {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        return principal;
    }
}
