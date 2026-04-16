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
@RequestMapping("/tournaments/{tournamentId}/rules")
@RequiredArgsConstructor
public class RulesController {

	private final IRulesService rulesService;

    @GetMapping
    public ResponseEntity<TournamentRulesDTO> getByTournament(@PathVariable("tournamentId") Long tournamentId) {
        return ResponseEntity.ok(rulesService.getByTournament(tournamentId));
    }    

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> create(
            @PathVariable("tournamentId") Long tournamentId,
            @Valid @RequestBody TournamentRulesCreateUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rulesService.create(tournamentId, dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> update(
            @PathVariable("tournamentId") Long tournamentId,
            @Valid @RequestBody TournamentRulesCreateUpdateDTO dto) {
        return ResponseEntity.ok(rulesService.update(tournamentId, dto));
    }

    @PostMapping("/clone-from/{sourceTournamentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentRulesDTO> cloneFromTournament(
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("sourceTournamentId") Long sourceTournamentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rulesService.cloneFromTournament(tournamentId, sourceTournamentId));
    }
}
