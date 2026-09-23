package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class ScoreAggregatorTest {

	private List<CoachResult> results;
	
	@BeforeEach
	void setUp() {
		Coach coach1 = new Coach();
		Coach coach2 = new Coach();
		coach1.setId(1L);
		coach2.setId(2L);
		
		CoachResult coachResult1 = new CoachResult();
		coachResult1.setId(1L);
		coachResult1.setResult(MatchResult.WIN);
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);		
		coachResult1.setCoach(coach1);
		
		CoachResult coachResult2 = new CoachResult();
		coachResult2.setId(2L);
		coachResult2.setResult(MatchResult.DRAW);
		coachResult2.setTouchdowns(1);
		coachResult2.setCasualties(0);
		coachResult2.setObjectives(3);
		coachResult2.setBonusObjective(false);
		coachResult2.setPasses(1);
		coachResult2.setFoulActions(3);
		coachResult2.setCoach(coach1);
		
		CoachResult coachResult3 = new CoachResult();
		coachResult3.setId(3L);
		coachResult3.setResult(MatchResult.DRAW);
		coachResult3.setTouchdowns(0);
		coachResult3.setCasualties(0);
		coachResult3.setObjectives(1);
		coachResult3.setBonusObjective(false);
		coachResult3.setPasses(2);
		coachResult3.setFoulActions(1);
		coachResult3.setCoach(coach2);
		
		results = new ArrayList<CoachResult>();
		results.add(coachResult1);
		results.add(coachResult2);
		results.add(coachResult3);
	}
	
	@Test
	void shouldReturnOneForNumberofWins() {
		// Arrange
		ScoreAggregator aggregator = new ScoreAggregator();		
		
		// Act
		List<ScoreDTO> scoreList = aggregator.aggregate(results);
		
		// Assert
		assertEquals(2, scoreList.size());
		assertEquals(1, findByCoachId(scoreList, 1L).getNumberOfWins());
		
		
	}
	
	private ScoreDTO findByCoachId(List<ScoreDTO> results, Long coachId) {
		return results.stream().filter(s -> s.getCoachId().equals(coachId)).findFirst().orElseThrow();
	}
}
