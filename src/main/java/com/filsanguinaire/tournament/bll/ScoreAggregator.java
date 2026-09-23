package com.filsanguinaire.tournament.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class ScoreAggregator {
	public List<ScoreDTO> aggregate(List<CoachResult> results) {
		Map<Long, ScoreDTO> scoresByCoachId = new HashMap<>();

		for (CoachResult cr : results) {
			long id = cr.getCoach().getId();
			
			int win = switch (cr.getResult()) {
			case WIN -> 1;
			default -> 0;
			};
			int draw = switch (cr.getResult()) {
			case DRAW -> 1;
			default -> 0;
			};
			
			int objectives = cr.isBonusObjective() ? 1 : 0;
			objectives += cr.getObjectives();
			
			ScoreDTO score = scoresByCoachId.computeIfAbsent(id, k -> ScoreDTO.builder().coachId(k).build());
		 
			score.setNumberOfWins(score.getNumberOfWins() + win);
			score.setNumberOfDraws(score.getNumberOfDraws() + draw);
			score.setNumberOfObjectives(score.getNumberOfObjectives() + objectives);
			score.setNumberOfTouchdowns(score.getNumberOfTouchdowns() + cr.getTouchdowns());
			score.setNumberOfCasualties(score.getNumberOfCasualties() + cr.getCasualties());
			score.setNumberOfPasses(score.getNumberOfPasses() + cr.getPasses());
			score.setNumberOfFoulActions(score.getNumberOfFoulActions() + cr.getFoulActions());
		}

		return new ArrayList<>(scoresByCoachId.values());
	}
}
