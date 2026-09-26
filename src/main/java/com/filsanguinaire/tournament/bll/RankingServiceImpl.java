package com.filsanguinaire.tournament.bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.CoachResultRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dto.ranking.RankingsDTO;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements IRankingService {

	private final CoachResultRepository coachResultRepository;

	private final CoachRepository coachRepository;

	private final TournamentRulesRepository tournamentRulesRepository;	

	@Override
	public RankingsDTO getRankings(Long tournamentId) {
		// Préparation pour Minus
		TournamentRules rules = tournamentRulesRepository.findByTournamentId(tournamentId).orElseThrow(
				() -> new IllegalStateException("Aucun ruleset configuré pour le tournoi  " + tournamentId));
		
		Map<String, Boolean> isMinusByRace = new HashMap<>();
		rules.getRosterCategories().forEach(roster -> isMinusByRace.put(roster.getRaceName(), roster.isMinus()));
		
		// Récupération de la liste des coachResult
		List<CoachResult> results = coachResultRepository.findAllByMatch_Round_Event_Id(tournamentId);
		
		// Agrégation
		ScoreAggregator aggregator = new ScoreAggregator();
		List<ScoreDTO> scores = aggregator.aggregate(results, isMinusByRace);
		
		// Classement
		RankingSorter sorter = new RankingSorter();
		
		return RankingsDTO	.builder()
							.generalRanking(sorter.finalSort(scores))
							.build();
	}

}
