package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.LoginRequest;
import com.bugwarsBackend.bugwars.dto.request.SignupRequest;
import com.bugwarsBackend.bugwars.dto.request.TokenRefreshRequest;
import com.bugwarsBackend.bugwars.dto.response.JwtResponse;
import com.bugwarsBackend.bugwars.dto.response.TokenRefreshResponse;
import com.bugwarsBackend.bugwars.exception.TokenRefreshException;
import com.bugwarsBackend.bugwars.model.ERole;
import com.bugwarsBackend.bugwars.model.Role;
import com.bugwarsBackend.bugwars.model.RefreshToken;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.RoleRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import com.bugwarsBackend.bugwars.security.jwt.JwtUtils;
import com.bugwarsBackend.bugwars.security.service.RefreshTokenService;
import com.bugwarsBackend.bugwars.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    public User registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken");
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Optional<Role> optUserRole = roleRepository.findByName(ERole.ROLE_USER);
        if (optUserRole.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.");
        }
        Role userRole = optUserRole.get();
        user.setRoles(Set.of(userRole));
        return userRepository.save(user);
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUsername(), roles);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                "Refresh token is not in database!"));
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String token = jwtUtils.generateTokenFromUsername(user.getUsername());

        logger.info("Token refreshed");

        return new TokenRefreshResponse(token, requestRefreshToken);
    }

    public void logout(Principal principal) {
        if (principal == null) return;

        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        refreshTokenService.deleteByUserId(user.getId());
    }
}
