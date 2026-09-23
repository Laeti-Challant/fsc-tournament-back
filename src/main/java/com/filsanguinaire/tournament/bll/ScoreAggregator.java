package com.filsanguinaire.tournament.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class ScoreAggregator {
	public List<ScoreDTO> aggregate(List<CoachResult> results) {
		Map<Long, ScoreDTO> scoresByCoachId = new HashMap<>();

		for (CoachResult cr : results) {
			long id = cr.getCoach().getId();

			int win = cr.getResult() == MatchResult.WIN ? 1 : 0;
			
			ScoreDTO score = scoresByCoachId.computeIfAbsent(id, k -> ScoreDTO.builder().coachId(k).build());
		 
			score.setNumberOfWins(score.getNumberOfWins() + win);
		}

		return new ArrayList<>(scoresByCoachId.values());
	}
}
