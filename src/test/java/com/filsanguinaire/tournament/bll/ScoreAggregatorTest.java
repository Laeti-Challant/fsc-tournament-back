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

	private List<CoachResult> list;
	
	@BeforeEach
	void setUp() {
		CoachResult coachResult1 = new CoachResult();
		coachResult1.setId(1L);
		coachResult1.setResult(MatchResult.WIN);
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		Coach coach = new Coach();
		coach.setId(1L);
		coachResult1.setCoach(coach);
		
		CoachResult coachResult2 = new CoachResult();
		coachResult2.setId(2L);
		coachResult2.setResult(MatchResult.DRAW);
		coachResult2.setTouchdowns(1);
		coachResult2.setCasualties(0);
		coachResult2.setObjectives(3);
		coachResult2.setBonusObjective(false);
		coachResult2.setPasses(1);
		coachResult2.setFoulActions(3);
		coachResult2.setCoach(coach);
		
		list = new ArrayList<CoachResult>();
		list.add(coachResult1);
		list.add(coachResult2);
	}
	
	@Test
	void shouldReturnOneForNumberofWins() {
		// Arrange
		ScoreAggregator aggregator = new ScoreAggregator();
		
		
		// Act
		List<ScoreDTO> scoreList = aggregator.aggregate(list);
		
		// Assert
		assertEquals(1, scoreList.size());
		assertEquals(1,  scoreList.getFirst().getNumberOfWins());
		
	}
}
