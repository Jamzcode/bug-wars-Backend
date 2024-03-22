package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.LoginRequest;
import com.bugwarsBackend.bugwars.dto.request.SignupRequest;
import com.bugwarsBackend.bugwars.dto.request.TokenRefreshRequest;
import com.bugwarsBackend.bugwars.dto.response.JwtResponse;
import com.bugwarsBackend.bugwars.dto.response.TokenRefreshResponse;
import com.bugwarsBackend.bugwars.exception.TokenRefreshException;
import com.bugwarsBackend.bugwars.model.ERole;
import com.bugwarsBackend.bugwars.model.RefreshToken;
import com.bugwarsBackend.bugwars.model.Role;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.RoleRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import com.bugwarsBackend.bugwars.security.jwt.JwtUtils;
import com.bugwarsBackend.bugwars.security.service.RefreshTokenService;
import com.bugwarsBackend.bugwars.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterUser_Successful() {
        SignupRequest request = new SignupRequest
                ("username", "email@example.com", "password");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn
                (Optional.of(new Role(1, ERole.ROLE_USER)));

        User registeredUser = authService.registerUser(request);

        assertNotNull(registeredUser);
        assertEquals(request.getUsername(), registeredUser.getUsername());
        assertEquals(request.getEmail(), registeredUser.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyTaken() {
        SignupRequest request = new SignupRequest
                ("existingUsername", "email@example.com", "password");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> authService.registerUser(request));
    }

    @Test
    public void testRegisterUser_EmailAlreadyTaken() {
        SignupRequest request = new SignupRequest
                ("username", "existingEmail@example.com", "password");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> authService.registerUser(request));
    }

    @Test
    public void testRegisterUser_InternalServerError() {
        SignupRequest request = new SignupRequest
                ("username", "email@example.com", "password");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> authService.registerUser(request));
    }

    @Test
    public void testAuthenticateUser_Successful() {
        LoginRequest request = new LoginRequest("username", "password");
        Authentication authentication = mock(Authentication.class);
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new UserDetailsImpl
                ((long) 1, "username", "password", authorities);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        RefreshToken refreshToken = new RefreshToken();
        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(refreshToken);
        JwtResponse jwtResponse = authService.authenticateUser(request);

        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getAccessToken());

        verify(refreshTokenService, times(1)).createRefreshToken(anyLong());
    }

    @Test
    public void testAuthenticateUser_AuthenticationFailure() {
        LoginRequest request = new LoginRequest("nonExistingUser", "password");
        when(authenticationManager.authenticate(any())).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticateUser(request));
    }

    @Test
    public void testRefreshToken_Successful() {
        Instant currentTime = Instant.now();
        Instant expiryDate = currentTime.plusSeconds(3600);
        TokenRefreshRequest request = new TokenRefreshRequest("validRefreshToken");
        RefreshToken refreshToken = new RefreshToken
                ((long)1, new User("username", "email@example.com", "password"), "AccessToken", expiryDate);
        when(refreshTokenService.findByToken
                (request.getRefreshToken())).thenReturn(Optional.of(refreshToken));
        when(jwtUtils.generateTokenFromUsername
                (refreshToken.getUser().getUsername())).thenReturn("newToken");

        TokenRefreshResponse refreshResponse = authService.refreshToken(request);

        assertNotNull(refreshResponse);
        assertEquals("newToken", refreshResponse.getAccessToken());
        assertEquals(request.getRefreshToken(), refreshResponse.getRefreshToken());

        verify(refreshTokenService, times(1)).findByToken(anyString());
        verify(refreshTokenService, times(1)).verifyExpiration(refreshToken);
    }

    @Test
    public void testRefreshToken_TokenNotFound() {
        TokenRefreshRequest request = new TokenRefreshRequest("nonExistingToken");
        when(refreshTokenService
                .findByToken(request.getRefreshToken())).thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(request));
    }

    @Test
    public void testRefreshToken_TokenExpired() {
        Instant currentTime = Instant.now();
        Instant expiryDate = currentTime.minusSeconds(3600);
        TokenRefreshRequest request = new TokenRefreshRequest("expiredToken");
        RefreshToken expiredToken = new RefreshToken
                ((long)1, new User("username", "email@example.com", "password"), "InvalidAccessToken", expiryDate);
        when(refreshTokenService
                .findByToken(request.getRefreshToken())).thenReturn(Optional.of(expiredToken));
        doThrow(TokenRefreshException.class)
                .when(refreshTokenService).verifyExpiration(expiredToken);

        assertThrows(TokenRefreshException.class, () -> authService.refreshToken(request));
    }
    @Test
    public void testLogout_PrincipalNull() {
        authService.logout(null);

        verify(userRepository, never()).findByUsername(anyString());
        verify(refreshTokenService, never()).deleteByUserId(anyLong());
    }

    @Test
    public void testLogout_UserFound() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");
        User user = new User("username", "email@example.com", "password");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        authService.logout(principal);

        verify(userRepository, times(1)).findByUsername("username");
        verify(refreshTokenService, times(1)).deleteByUserId(user.getId());
    }

    @Test
    public void testLogout_UserNotFound() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("nonExistingUser");
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> authService.logout(principal));

        verify(userRepository, times(1)).findByUsername("nonExistingUser");
        verify(refreshTokenService, never()).deleteByUserId(anyLong());
    }
}
