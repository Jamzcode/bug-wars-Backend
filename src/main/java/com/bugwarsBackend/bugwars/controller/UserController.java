package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD

import java.util.List;
import java.util.Optional;

=======
>>>>>>> 797f2edcf3d72e8de13e8cdb00dcd12efc621b7e
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

<<<<<<< HEAD

=======
>>>>>>> 797f2edcf3d72e8de13e8cdb00dcd12efc621b7e
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
<<<<<<< HEAD
=======

>>>>>>> 797f2edcf3d72e8de13e8cdb00dcd12efc621b7e
    @GetMapping("/{user_id}")
    public Optional<User> getUserById(@PathVariable("user_id") long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{user_id}")
    public User updateUser(@PathVariable("user_id") long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable("user_id") long id) {
        userService.deleteUser(id);
    }
}