package com.filsanguinaire.tournament.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.IRankingService;
import com.filsanguinaire.tournament.dto.ranking.RankingsDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rankings")
@RequiredArgsConstructor
public class RankingController {

	private final IRankingService rankingService;
	
	@GetMapping("/{tournamentId}")
	ResponseEntity<RankingsDTO> getRanking(@PathVariable("tournamentId") Long tournamentId) {
		return ResponseEntity.ok(rankingService.getRankings(tournamentId));
	}
}
