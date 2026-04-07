package com.filsanguinaire.tournament.bll;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.auth.AuthResponseDTO;
import com.filsanguinaire.tournament.dto.auth.LoginDTO;
import com.filsanguinaire.tournament.dto.auth.RegisterDTO;
import com.filsanguinaire.tournament.security.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Override
    public AuthResponseDTO register(RegisterDTO dto, HttpServletResponse response) {
        // Vérification que l'email n'est pas déjà utilisé
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }
        // Vérification que le pseudo n'est pas déjà utilisé
        if (userRepository.existsByPseudo(dto.getPseudo())) {
            throw new PseudoAlreadyExistsException(dto.getPseudo());
        }

        // Construction de l'entité User
        User user = User.builder()
                .email(dto.getEmail())
                .pseudo(dto.getPseudo())
                // Hashage du mot de passe
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                // Rôle PLAYER par défaut
                .build();

        // Sauvegarde en base
        userRepository.save(user);
        log.debug("Nouvel utilisateur enregistré : {}", user.getEmail());

        // Génération du token et pose du cookie
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        addJwtCookie(response, token);

        return AuthResponseDTO.builder()
                .pseudo(user.getPseudo())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto, HttpServletResponse response) {
        // Chargement de l'utilisateur depuis la base
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(dto.getEmail()));

        // Vérification du mot de passe
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        // Vérification que le compte est actif
        if (!user.isActive()) {
            throw new DisabledException("Compte désactivé");
        }

        // Mise à jour de lastLogin
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        log.debug("Connexion réussie : {}", user.getEmail());

        // Génération du token et pose du cookie
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        addJwtCookie(response, token);

        return AuthResponseDTO.builder()
                .pseudo(user.getPseudo())
                .role(user.getRole())
                .build();
    }

    @Override
    public void logout(HttpServletResponse response) {
        // Suppression du cookie en le vidant avec maxAge 0
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("Déconnexion — cookie JWT supprimé");
    }

    // Méthode privée réutilisable pour poser le cookie JWT
    private void addJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
