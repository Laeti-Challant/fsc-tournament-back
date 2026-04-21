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

import com.filsanguinaire.tournament.bll.IRoundService;
import com.filsanguinaire.tournament.dto.round.RoundDTO;
import com.filsanguinaire.tournament.dto.round.RoundUpdateDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/rounds")
@RequiredArgsConstructor
public class RoundController {

	private final IRoundService roundService;
	
	@GetMapping
    public ResponseEntity<List<RoundDTO>> findAll(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok(roundService.findAllByEvent(eventId));
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<RoundDTO> findById(
            @PathVariable("eventId") Long eventId,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(roundService.findByIdAndEvent(id, eventId));
    }
	
	@PostMapping("/generate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<RoundDTO>> generateAll(@PathVariable Long eventId) {
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(roundService.generateAll(eventId));
	}
	
	@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoundDTO> update(
            @PathVariable Long eventId,
            @PathVariable Long id,
            @Valid @RequestBody RoundUpdateDTO dto) {
        return ResponseEntity.ok(roundService.update(id, eventId, dto));
    }
}
