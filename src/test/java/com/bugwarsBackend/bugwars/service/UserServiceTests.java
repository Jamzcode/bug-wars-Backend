package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import com.bugwarsBackend.bugwars.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsers_returnsAllUsers() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("password1")
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertThat(users).hasSize(1).contains(user);
    }

    @Test
    public void updateUser_returnsUpdatedUser() {
        // arrange
        User existingUser = User.builder()
                .user_id(12343L) // Set the user_id to match your existing user
                .firstName("ExistingFirstName")
                .lastName("ExistingLastName")
                .username("ExistingUsername")
                .password("ExistingPassword")
                .build();

        User updatedUser = User.builder()
                .user_id(12343L) // Set an example user_id here
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .username("UpdatedUsername")
                .password("UpdatedPassword")
                .build();

        when(userRepository.findById(12343L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        // act
        User result = userService.updateUser(12343, updatedUser);

        // assert
        assertThat(result).isEqualTo(updatedUser);
    }

}
