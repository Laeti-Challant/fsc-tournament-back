package com.filsanguinaire.tournament.bll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.filsanguinaire.tournament.dto.user.UserAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.user.UserDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdateEmailDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePasswordDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePseudoDTO;

public interface IUserService extends UserDetailsService {

	// ── Profil utilisateur connecté ──────────────────────────
    UserDTO getMe(String email);
    UserDTO updateEmail(String email, UserUpdateEmailDTO dto);
    UserDTO updatePseudo(String email, UserUpdatePseudoDTO dto);
    UserDTO updatePassword(String email, UserUpdatePasswordDTO dto);

    // ── Administration ───────────────────────────────────────
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO updateUserByAdmin(Long id, UserAdminUpdateDTO dto);
}
