package com.filsanguinaire.tournament.bll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.user.UserAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.user.UserDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdateEmailDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePasswordDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePseudoDTO;
import com.filsanguinaire.tournament.exceptions.EmailAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.PseudoAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.UserNotFoundException;
import com.filsanguinaire.tournament.security.CookieService;
import com.filsanguinaire.tournament.security.JwtService;
import com.filsanguinaire.tournament.security.UserPrincipal;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtService jwtService;
	
	private final CookieService cookieService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable : " + email));
        return new UserPrincipal(user);
	}

	@Override
	public UserDTO getMe(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
		return toDTO(user);
	}

	@Override
	public UserDTO updateEmail(String email, UserUpdateEmailDTO dto, HttpServletResponse response) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException(email));

	    if (userRepository.existsByEmail(dto.getEmail())) {
	        throw new EmailAlreadyExistsException(dto.getEmail());
	    }

	    user.setEmail(dto.getEmail());
	    userRepository.save(user);

	    // Nouveau cookie avec le nouvel email
	    String newToken = jwtService.generateToken(dto.getEmail(), user.getRole().name());
	    cookieService.addJwtCookie(response, newToken);

	    log.debug("Email mis à jour pour : {} → {}", email, dto.getEmail());
	    return toDTO(user);
	}

	@Override
	public UserDTO updatePseudo(String email, UserUpdatePseudoDTO dto) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // Vérification que le nouveau pseudo n'est pas déjà pris
        if (userRepository.existsByPseudo(dto.getPseudo())) {
            throw new PseudoAlreadyExistsException(dto.getPseudo());
        }

        user.setPseudo(dto.getPseudo());
        userRepository.save(user);
        log.debug("Pseudo mis à jour pour : {}", email);
        return toDTO(user);
	}

	@Override
	public UserDTO updatePassword(String email, UserUpdatePasswordDTO dto) {
		User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // Hashage du nouveau mot de passe avant sauvegarde
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        log.debug("Mot de passe mis à jour pour : {}", email);
        return toDTO(user);
	}

	
	@Override
	public Page<UserDTO> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable).map(this::toDTO);
	}

	@Override
	public UserDTO updateUserByAdmin(Long id, UserAdminUpdateDTO dto) {
		User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id: " + id));

        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
        userRepository.save(user);
        log.debug("User {} mis à jour par admin", id);
        return toDTO(user);
	}

	
	private UserDTO toDTO(User user) {
		return UserDTO.builder()
                .id(user.getId())
                .pseudo(user.getPseudo())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
	}
}
