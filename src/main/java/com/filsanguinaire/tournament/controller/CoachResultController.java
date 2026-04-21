package com.filsanguinaire.tournament.controller;

import java.util.List;

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

import com.filsanguinaire.tournament.bll.ICoachResultService;
import com.filsanguinaire.tournament.dto.result.CoachResultDTO;
import com.filsanguinaire.tournament.dto.result.MatchResultCreateDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventid}/rounds/{roundId}/matches/{matchId}/results")
@RequiredArgsConstructor
public class CoachResultController {

	private final ICoachResultService coachResultService;
	
	@GetMapping
    public ResponseEntity<List<CoachResultDTO>> getByMatch(
            @PathVariable("eventId") Long eventId,
            @PathVariable("roundId") Long roundId,
            @PathVariable("matchId") Long matchId) {

        return ResponseEntity.ok(
            coachResultService.getByMatch(eventId, roundId, matchId)
        );
    }
	
	@PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoachResultDTO>> create(
            @PathVariable Long eventId,
            @PathVariable Long roundId,
            @PathVariable Long matchId,
            @RequestBody @Valid MatchResultCreateDTO dto) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(coachResultService.create(eventId, roundId, matchId, dto));
    }
	
	@PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CoachResultDTO>> update(
            @PathVariable Long eventId,
            @PathVariable Long roundId,
            @PathVariable Long matchId,
            @RequestBody @Valid MatchResultCreateDTO dto) {

        return ResponseEntity.ok(
            coachResultService.update(eventId, roundId, matchId, dto)
        );
    }
}
