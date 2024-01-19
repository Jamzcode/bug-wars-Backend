package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.dto.request.LoginRequest;
import com.bugwarsBackend.bugwars.dto.request.SignupRequest;
import com.bugwarsBackend.bugwars.dto.request.TokenRefreshRequest;
import com.bugwarsBackend.bugwars.dto.response.JwtResponse;
import com.bugwarsBackend.bugwars.dto.response.TokenRefreshResponse;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/refresh-token")
    public TokenRefreshResponse refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(Principal principal) {
        authService.logout(principal);
    }
}

