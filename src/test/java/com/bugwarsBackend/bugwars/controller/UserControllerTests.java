package com.bugwarsBackend.bugwars.controller;
import com.bugwarsBackend.bugwars.controller.UserController;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void getAllUsers_ReturnsListOfUsers() throws Exception {
        // mocking the behavior of the userService
        when(userService.getAllUsers()).thenReturn(List.of(
                new User(1L, "John", "Doe", "john.doe", "password123"),
                new User(2L, "Jane", "Doe", "jane.doe", "password456")
        ));

        // performing a GET request and verifying the response
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)) // Assuming your JSON response is an array
                .andExpect(jsonPath("$[0].username").value("john.doe"))
                .andExpect(jsonPath("$[1].username").value("jane.doe"));
    }

    @Test
    public void getUserById_UserExists_ReturnsUser() throws Exception {
        // mocking the behavior of the userService
        when(userService.getUserById(1L)).thenReturn(Optional.of(new User(3L, "John", "Doe", "john.doe", "password123")));

        // performing a GET request and verifying the response
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("john.doe"));
    }
}

