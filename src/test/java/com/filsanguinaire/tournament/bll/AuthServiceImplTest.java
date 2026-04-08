package com.filsanguinaire.tournament.bll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.filsanguinaire.tournament.bo.Role;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.auth.AuthResponseDTO;
import com.filsanguinaire.tournament.dto.auth.LoginDTO;
import com.filsanguinaire.tournament.dto.auth.RegisterDTO;
import com.filsanguinaire.tournament.exceptions.EmailAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.PseudoAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.UserNotFoundException;
import com.filsanguinaire.tournament.security.JwtService;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

	// --- Mocks des dépendances ---
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletResponse response;

    // --- Classe à tester avec les mocks injectés ---
    @InjectMocks
    private AuthServiceImpl authService;

    // --- Données de test réutilisables ---
    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private User activeUser;

    @BeforeEach
    void setUp() {
        // Données d'inscription
        registerDTO = new RegisterDTO();
        registerDTO.setEmail("coach@fsc.fr");
        registerDTO.setPseudo("LaeyaCoach");
        registerDTO.setPassword("password123");

        // Données de connexion
        loginDTO = new LoginDTO();
        loginDTO.setEmail("coach@fsc.fr");
        loginDTO.setPassword("password123");

        // User actif en base
        activeUser = User.builder()
                .id(1L)
                .email("coach@fsc.fr")
                .pseudo("LaeyaCoach")
                .passwordHash("$2a$hashed")
                .role(Role.PLAYER)
                .active(true)
                .build();
    }

    // =====================
    // TESTS REGISTER
    // =====================

    @Test
    @DisplayName("Register — succès : nouvel utilisateur créé")
    void register_success() {
        // GIVEN
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByPseudo(registerDTO.getPseudo())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("fake.jwt.token");

        // WHEN
        AuthResponseDTO result = authService.register(registerDTO, response);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getPseudo()).isEqualTo("LaeyaCoach");
        assertThat(result.getRole()).isEqualTo(Role.PLAYER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register — échec : email déjà utilisé")
    void register_emailAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        // WHEN + THEN
        assertThatThrownBy(() -> authService.register(registerDTO, response))
                .isInstanceOf(EmailAlreadyExistsException.class);

        // On vérifie qu'aucun user n'a été sauvegardé
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Register — échec : pseudo déjà utilisé")
    void register_pseudoAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByPseudo(registerDTO.getPseudo())).thenReturn(true);

        // WHEN + THEN
        assertThatThrownBy(() -> authService.register(registerDTO, response))
                .isInstanceOf(PseudoAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    // =====================
    // TESTS LOGIN
    // =====================

    @Test
    @DisplayName("Login — succès : token généré et cookie posé")
    void login_success() {
        // GIVEN
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(loginDTO.getPassword(), activeUser.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("fake.jwt.token");

        // WHEN
        AuthResponseDTO result = authService.login(loginDTO, response);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getPseudo()).isEqualTo("LaeyaCoach");
        assertThat(result.getRole()).isEqualTo(Role.PLAYER);
    }

    @Test
    @DisplayName("Login — échec : email inconnu")
    void login_userNotFound() {
        // GIVEN
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> authService.login(loginDTO, response))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Login — échec : mauvais mot de passe")
    void login_badCredentials() {
        // GIVEN
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(loginDTO.getPassword(), activeUser.getPasswordHash())).thenReturn(false);

        // WHEN + THEN
        assertThatThrownBy(() -> authService.login(loginDTO, response))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("Login — échec : compte désactivé")
    void login_accountDisabled() {
        // GIVEN
        User disabledUser = User.builder()
                .email("coach@fsc.fr")
                .pseudo("LaeyaCoach")
                .passwordHash("$2a$hashed")
                .role(Role.PLAYER)
                .active(false) // compte désactivé
                .build();

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(disabledUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // WHEN + THEN
        assertThatThrownBy(() -> authService.login(loginDTO, response))
                .isInstanceOf(DisabledException.class);
    }
}
