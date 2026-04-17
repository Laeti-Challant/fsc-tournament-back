package com.filsanguinaire.tournament.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.ITournamentService;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.tournament.TournamentCreateUpdateDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

	private final ITournamentService tournamentService;
	
	@GetMapping("/current")
	public ResponseEntity<EventDetailDTO> getCurrentTournament() {
		return ResponseEntity.ok(tournamentService.getCurrentTournament());
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventDetailDTO> create(@Valid @RequestBody TournamentCreateUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentService.create(dto));
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventDetailDTO> update(@PathVariable("id") Long id, @Valid @RequestBody TournamentCreateUpdateDTO dto) {
		return ResponseEntity.ok(tournamentService.update(id, dto));
	}
}
