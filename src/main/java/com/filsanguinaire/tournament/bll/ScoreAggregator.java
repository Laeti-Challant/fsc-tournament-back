package com.filsanguinaire.tournament.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class ScoreAggregator {
	public List<ScoreDTO> aggregate(List<CoachResult> list) {
		Map<Long, ScoreDTO> map = new HashMap<>();
		
		for (CoachResult cr : list) {
			 long id = cr.getCoach().getId();
			 
			 if (!map.containsKey(id)) {
				 
				 ScoreDTO score = ScoreDTO.builder()
						 .coachId(id)
						 .numberOfWins(cr.getResult() == MatchResult.WIN ? 1 : 0)
						 
						 .build();
				 map.put(id, score);				
			 } else {
				 
				 ScoreDTO oldScore = map.get(id);
				 int wins = oldScore.getNumberOfWins();
				 				 
				 int winsToAdd = cr.getResult() == MatchResult.WIN ? 1 : 0;
				 
				 
				 ScoreDTO newScore = ScoreDTO.builder()
						 .coachId(id)
						 .numberOfWins(wins + winsToAdd)						 
						 .build();
				 
				 map.put(id, newScore);
			 }
		 }
		
		List<ScoreDTO> scores = new ArrayList<>(map.values());
		return scores;
	}
}
