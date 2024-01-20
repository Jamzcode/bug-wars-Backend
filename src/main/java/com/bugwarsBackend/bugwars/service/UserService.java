package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(String firstName, String lastName, String username, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long user_id) {
        return userRepository.findById(user_id);
    }

    public User createUser(User user) {
        // Save the new user
        return userRepository.save(user);
    }

    public User updateUser(long user_id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // Update user properties
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail((updatedUser.getEmail()));
            existingUser.setPassword(updatedUser.getPassword());
            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            // Handle user not found
            return null;
        }
    }

    public void deleteUser(long user_id) {
        userRepository.deleteById(user_id);
    }
}
