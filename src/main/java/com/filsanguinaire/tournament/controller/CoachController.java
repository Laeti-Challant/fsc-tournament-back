package com.filsanguinaire.tournament.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.ICoachService;
import com.filsanguinaire.tournament.dto.coach.CoachAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;
import com.filsanguinaire.tournament.security.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tournaments/{tournamentId}/coaches")
@RequiredArgsConstructor
public class CoachController {

	private final ICoachService coachService;
	
	@GetMapping
    public ResponseEntity<Page<CoachSummaryDTO>> getAll(
            @PathVariable Long tournamentId,
            Pageable pageable) {
        return ResponseEntity.ok(coachService.getAllByTournament(tournamentId, pageable));
    }

	@GetMapping("/me")
	public ResponseEntity<CoachDetailDTO> getMyCoach(
	        @PathVariable Long tournamentId,
	        Authentication authentication) {
	    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
	    return coachService.getMyCoach(tournamentId, principal.getId()).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CoachDetailDTO> getById(
	        @PathVariable Long tournamentId,
	        @PathVariable Long id) {
	    return ResponseEntity.ok(coachService.getById(tournamentId, id));
	}	

    @GetMapping("/meals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CoachDetailDTO>> getMeals(
            @PathVariable Long tournamentId,
            Pageable pageable) {
        return ResponseEntity.ok(coachService.getMealsByTournament(tournamentId, pageable));
    }

    @PostMapping
    public ResponseEntity<CoachDetailDTO> register(
            @PathVariable Long tournamentId,
            @RequestBody @Valid CoachCreateDTO dto,
            Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(coachService.register(tournamentId, principal.getId(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachDetailDTO> adminUpdate(
            @PathVariable Long tournamentId,
            @PathVariable Long id,
            @RequestBody @Valid CoachAdminUpdateDTO dto) {
        return ResponseEntity.ok(coachService.adminUpdate(tournamentId, id, dto));
    }
    
    @PutMapping("/me")
    public ResponseEntity<CoachDetailDTO> updateMe(
            @PathVariable Long tournamentId,
            @RequestBody @Valid CoachUpdateDTO dto,
            Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(coachService.updateMe(tournamentId, principal.getId(), dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long tournamentId,
            @PathVariable Long id) {
        coachService.delete(tournamentId, id);
        return ResponseEntity.noContent().build();
    }
}
