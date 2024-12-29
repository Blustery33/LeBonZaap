package org.example.back.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.back.config.JwtService;
import org.example.back.config.RefreshTokenService;
import org.example.back.config.TestSecurityConfig;
import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.example.back.models.enums.UserType;
import org.example.back.repository.RefreshTokenRepository;
import org.example.back.repository.UserRepository;
import org.example.back.services.AuthService;
import org.example.back.services.impl.AuthServiceImpl;
import org.example.back.services.record.LoginResponse;
import org.example.back.services.record.RefreshTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(TestSecurityConfig.class)
public class AuthServiceTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthServiceImpl authService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialise les mocks
    }

    @Test
    public void testLoginSuccess() {
        // Crée un objet UserModel simulant l'utilisateur qui tente de se connecter
        UserModel user = new UserModel();
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Crée un utilisateur existant en base de données avec les mêmes informations
        UserModel existingUser = new UserModel();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");

        // Crée un objet RefreshToken simulant un token de rafraîchissement
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refreshToken123");

        // Simule la recherche de l'utilisateur dans la base de données et retourne l'utilisateur existant
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        // Simule la génération d'un jeton JWT pour l'utilisateur
        when(jwtService.generateToken(user)).thenReturn("jwtToken123");

        // Simule la création d'un refresh token pour l'utilisateur authentifié
        when(refreshTokenService.createRefreshToken(existingUser)).thenReturn(refreshToken);

        // Exécute la méthode login() avec l'utilisateur
        LoginResponse response = authService.login(user);

        // Vérifie que la réponse n'est pas nulle (la connexion est réussie)
        assertNotNull(response);

        // Vérifie que le jeton JWT retourné correspond à celui attendu
        assertEquals("jwtToken123", response.token());

        // Vérifie que le token de rafraîchissement retourné correspond à celui attendu
        assertEquals("refreshToken123", response.refreshToken());
    }

    @Test
    public void testLoginUserNotFound() {
        // Crée un objet UserModel pour un utilisateur qui n'existe pas
        UserModel user = new UserModel();
        user.setEmail("notfound@example.com");
        user.setPassword("password");

        // Simule l'absence de l'utilisateur dans la base de données
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Vérifie que la méthode login() lève une exception UsernameNotFoundException
        // lorsqu'un utilisateur non existant tente de se connecter
        assertThrows(UsernameNotFoundException.class, () -> authService.login(user));
    }



    @Test
    public void testRefreshToken_WhenRefreshTokenNotValid_ShouldThrowException() {
        // Simulation d'un token invalide
        String invalidToken = "invalid_token";
        when(refreshTokenService.validateRefreshToken(invalidToken)).thenReturn(false);

        // On vérifie que l'exception est bien levée lorsque le token est invalide
        assertThrows(AuthenticationException.class, () -> {
            authService.refreshToken(invalidToken);
        });
    }

    @Test
    public void testRefreshToken_WhenValidToken_ShouldReturnNewToken() {
        // Simulation d'un token valide
        String validToken = "valid_token";
        String userId = "123";
        when(refreshTokenService.validateRefreshToken(validToken)).thenReturn(false);

        // Simulation de l'extraction de l'ID de l'utilisateur
        when(refreshTokenService.extractUserIdFromToken(validToken)).thenReturn(Long.valueOf(userId));

        // Simulation de la récupération de l'utilisateur
        UserModel user = new UserModel();
        user.setId(Long.valueOf(userId));
        when(userRepository.findById(Long.valueOf(userId))).thenReturn(Optional.of(user));

        // Simulation de la génération de nouveaux tokens
        String newAccessToken = "new_access_token";
        when(jwtService.generateToken(user)).thenReturn(newAccessToken);
        String newRefreshToken = "new_refresh_token";
        when(refreshTokenService.generateRefreshToken(user)).thenReturn(newRefreshToken);

        // Simulation de la mise à jour du refresh token dans la base
        RefreshToken refreshToken = new RefreshToken();
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));

        // Exécution de la méthode et vérification de la réponse
        RefreshTokenResponse response = authService.refreshToken(validToken);

        assertNotNull(response);
        assertEquals("ok", response.message());
        assertEquals(validToken, response.refreshToken());
    }
}

