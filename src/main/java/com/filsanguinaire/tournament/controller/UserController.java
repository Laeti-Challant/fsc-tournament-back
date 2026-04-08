package com.filsanguinaire.tournament.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.IUserService;
import com.filsanguinaire.tournament.dto.user.UserAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.user.UserDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdateEmailDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePasswordDTO;
import com.filsanguinaire.tournament.dto.user.UserUpdatePseudoDTO;
import com.filsanguinaire.tournament.security.UserPrincipal;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;
	
	@GetMapping("/me")
	public ResponseEntity<UserDTO>  getMe(@AuthenticationPrincipal UserPrincipal principal) {
		return ResponseEntity.ok(userService.getMe(principal.getUsername()));
	}
	
	@PutMapping("/me/email")
    public ResponseEntity<UserDTO> updateEmail(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserUpdateEmailDTO dto,
            HttpServletResponse response) {
        return ResponseEntity.ok(userService.updateEmail(principal.getUsername(), dto, response));
    }

    @PutMapping("/me/pseudo")
    public ResponseEntity<UserDTO> updatePseudo(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserUpdatePseudoDTO dto) {
        return ResponseEntity.ok(userService.updatePseudo(principal.getUsername(), dto));
    }

    @PutMapping("/me/password")
    public ResponseEntity<UserDTO> updatePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserUpdatePasswordDTO dto) {
        return ResponseEntity.ok(userService.updatePassword(principal.getUsername(), dto));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UserAdminUpdateDTO dto) {
        return ResponseEntity.ok(userService.updateUserByAdmin(id, dto));
    }
}
