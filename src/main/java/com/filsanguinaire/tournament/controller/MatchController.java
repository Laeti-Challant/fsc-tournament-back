package com.filsanguinaire.tournament.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.IMatchService;
import com.filsanguinaire.tournament.bll.IPairingService;
import com.filsanguinaire.tournament.dto.match.MatchCreateDTO;
import com.filsanguinaire.tournament.dto.match.MatchDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/rounds/{roundId}/matches")
@RequiredArgsConstructor
public class MatchController {

	private final IMatchService matchService;
	
	private final IPairingService pairingService;
	
	@GetMapping
    public ResponseEntity<List<MatchDTO>> findAll(
            @PathVariable("eventId") Long eventId,
            @PathVariable("roundId") Long roundId) {
        return ResponseEntity.ok(matchService.findAllByRound(eventId, roundId));
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findById(
            @PathVariable Long eventId,
            @PathVariable Long roundId,
            @PathVariable Long id) {
        return ResponseEntity.ok(matchService.findByIdAndRound(id, roundId, eventId));
    }
	
	@PostMapping("/challenges")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatchDTO> createChallenge(
            @PathVariable Long eventId,
            @PathVariable Long roundId,
            @Valid @RequestBody MatchCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.createChallenge(eventId, roundId, dto));
    }
	
	@PostMapping("/generate-pairings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MatchDTO>> generatePairings(
            @PathVariable Long eventId,
            @PathVariable Long roundId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pairingService.generatePairings(eventId, roundId));
    }
}
