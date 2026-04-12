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

import com.filsanguinaire.tournament.bll.IRulesService;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events/{eventId}/rules")
@RequiredArgsConstructor
public class RulesController {

	private final IRulesService rulesService;

    @GetMapping
    public ResponseEntity<TournamentRulesDTO> getByEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.ok(rulesService.getByEvent(eventId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> create(
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody TournamentRulesCreateUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rulesService.create(eventId, dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> update(
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody TournamentRulesCreateUpdateDTO dto) {
        return ResponseEntity.ok(rulesService.update(eventId, dto));
    }

    @PostMapping("/clone-from/{sourceEventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> cloneFromEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("sourceEventId") Long sourceEventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rulesService.cloneFromEvent(eventId, sourceEventId));
    }
}
