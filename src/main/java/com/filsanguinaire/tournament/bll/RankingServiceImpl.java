package com.filsanguinaire.tournament.bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.CoachStatus;
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
		
		// Pour chaque coach validé, s'il n'a pas de score lui en créer un à 0
		List<Long> listCoachId = coachRepository.findByEventIdAndStatus(tournamentId, CoachStatus.VALIDATED).stream().map(Coach::getId).toList();
		
		Set<Long> presentCoachIds = scores.stream().map(ScoreDTO::getCoachId).collect(Collectors.toSet());
		
		List<Long> missingCoachIds = listCoachId.stream().filter(id -> !presentCoachIds.contains(id)).toList();
		
		for (Long id: missingCoachIds) {
			scores.add(ScoreDTO.builder().coachId(id).build());
		}
		
		// Classement
		RankingSorter sorter = new RankingSorter();
		
		return RankingsDTO	.builder()
							.generalRanking(sorter.finalSort(scores))
							.build();
	}
}
