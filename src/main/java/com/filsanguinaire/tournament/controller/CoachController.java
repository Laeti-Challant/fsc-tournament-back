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
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;
import com.filsanguinaire.tournament.security.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/coaches")
@RequiredArgsConstructor
public class CoachController {

	private final ICoachService coachService;
	
	@GetMapping
    public ResponseEntity<Page<CoachSummaryDTO>> getAll(
            @PathVariable Long eventId,
            Pageable pageable) {
        return ResponseEntity.ok(coachService.getAllByEvent(eventId, pageable));
    }

	@GetMapping("/me")
	public ResponseEntity<CoachDetailDTO> getMyCoach(
	        @PathVariable Long eventId,
	        Authentication authentication) {
	    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
	    return ResponseEntity.ok(coachService.getMyCoach(eventId, principal.getId()));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CoachDetailDTO> getById(
	        @PathVariable Long eventId,
	        @PathVariable Long id) {
	    return ResponseEntity.ok(coachService.getById(eventId, id));
	}	

    @GetMapping("/meals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CoachDetailDTO>> getMeals(
            @PathVariable Long eventId,
            Pageable pageable) {
        return ResponseEntity.ok(coachService.getMealsByEvent(eventId, pageable));
    }

    @PostMapping
    public ResponseEntity<CoachDetailDTO> register(
            @PathVariable Long eventId,
            @RequestBody @Valid CoachCreateDTO dto,
            Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(coachService.register(eventId, principal.getId(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoachDetailDTO> adminUpdate(
            @PathVariable Long eventId,
            @PathVariable Long id,
            @RequestBody @Valid CoachUpdateDTO dto) {
        return ResponseEntity.ok(coachService.adminUpdate(eventId, id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long eventId,
            @PathVariable Long id) {
        coachService.delete(eventId, id);
        return ResponseEntity.noContent().build();
    }
}
