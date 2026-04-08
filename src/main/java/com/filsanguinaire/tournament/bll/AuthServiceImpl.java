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

import com.filsanguinaire.tournament.bo.Role;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.auth.AuthResponseDTO;
import com.filsanguinaire.tournament.dto.auth.LoginDTO;
import com.filsanguinaire.tournament.dto.auth.RegisterDTO;
import com.filsanguinaire.tournament.exceptions.EmailAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.PseudoAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.UserNotFoundException;
import com.filsanguinaire.tournament.security.CookieService;
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
    
    private final CookieService cookieService;    

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
                .role(Role.PLAYER)
                .active(true)
                .build();

        // Sauvegarde en base
        userRepository.save(user);
        log.debug("Nouvel utilisateur enregistré : {}", user.getEmail());

        // Génération du token et pose du cookie
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        cookieService.addJwtCookie(response, token);

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
        cookieService.addJwtCookie(response, token);

        return AuthResponseDTO.builder()
                .pseudo(user.getPseudo())
                .role(user.getRole())
                .build();
    }

    @Override
    public void logout(HttpServletResponse response) {
        cookieService.removeJwtCookie(response);
        log.debug("Déconnexion — cookie JWT supprimé");
    }

    

}
