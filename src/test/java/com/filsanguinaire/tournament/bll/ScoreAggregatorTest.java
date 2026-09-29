package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class ScoreAggregatorTest {

	private List<CoachResult> results;
	
	private Map<String, Boolean> mapIsMinus;

	@BeforeEach
	void setUp() {
		mapIsMinus = new HashMap<String, Boolean>();
		mapIsMinus.put("Orc", false);
		mapIsMinus.put("Gobelins", true);
		
		Coach coach1 = new Coach();
		Coach coach2 = new Coach();
		coach1.setId(1L);
		coach1.setRace("Gobelins");
		coach2.setId(2L);
		coach2.setRace("Orc");

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
		
		CoachResult coachResult4 = new CoachResult();
		coachResult4.setId(4L);
		coachResult4.setResult(MatchResult.LOSS);
		coachResult4.setTouchdowns(0);
		coachResult4.setCasualties(0);
		coachResult4.setObjectives(0);
		coachResult4.setBonusObjective(true);
		coachResult4.setPasses(0);
		coachResult4.setFoulActions(0);
		coachResult4.setCoach(coach1);

		results = new ArrayList<CoachResult>();
		results.add(coachResult1);
		results.add(coachResult2);
		results.add(coachResult3);
		results.add(coachResult4);
	}

	@Test
	void shouldReturnOneForNumberOfWins() {
		// Arrange
		ScoreAggregator aggregator = new ScoreAggregator();

		// Act
		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);

		// Assert
		assertEquals(1, findByCoachId(scoreList, 1L).getNumberOfWins());

	}

	@Test
	void shouldSumTouchdownsCasualtiesPassesFoulActionsForOneCoach() {
		ScoreAggregator aggregator = new ScoreAggregator();

		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);

		ScoreDTO scoreCoach1 = findByCoachId(scoreList, 1L);
		assertEquals(3, scoreCoach1.getNumberOfTouchdowns());
		assertEquals(1, scoreCoach1.getNumberOfCasualties());
		assertEquals(3, scoreCoach1.getNumberOfPasses());
		assertEquals(5, scoreCoach1.getNumberOfFoulActions());
	}

	@Test
	void shouldCountWinsAndDrawsOnly() {
		ScoreAggregator aggregator = new ScoreAggregator();

		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);
		
		ScoreDTO scoreCoach1 = findByCoachId(scoreList, 1L);
		
		assertEquals(1, scoreCoach1.getNumberOfWins());
		assertEquals(1, scoreCoach1.getNumberOfDraws());
	}
	
	@Test 
	void shouldCountObjectivesWithBonusObjective() {
		ScoreAggregator aggregator = new ScoreAggregator();

		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);
		
		ScoreDTO scoreCoach1 = findByCoachId(scoreList, 1L);
		ScoreDTO scoreCoach2 = findByCoachId(scoreList, 2L);
		
		assertEquals(7, scoreCoach1.getNumberOfObjectives());
		assertEquals(1, scoreCoach2.getNumberOfObjectives());
	}
	
	@Test
	void shouldReturnIsMinusFromRace() {
		ScoreAggregator aggregator = new ScoreAggregator();

		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);
		
		ScoreDTO scoreCoach1 = findByCoachId(scoreList, 1L);
		ScoreDTO scoreCoach2 = findByCoachId(scoreList, 2L);
		
		assertTrue(scoreCoach1.isMinus());
		assertFalse(scoreCoach2.isMinus());
	}
	
	@Test
	void shouldThrowWhenRaceNotInMap() {
		ScoreAggregator aggregator = new ScoreAggregator();
		Map<String, Boolean> mapWithoutOrc = new HashMap<String, Boolean>();
		mapWithoutOrc.put("Gobelins", true);
		
		IllegalStateException exceptionToAssert = assertThrows(IllegalStateException.class, () -> aggregator.aggregate(results, mapWithoutOrc));
		
		assertTrue(exceptionToAssert.getMessage().contains("Orc"));
		
	}
	
	@Test
	void shouldGroupResultsByCoach() {
		ScoreAggregator aggregator = new ScoreAggregator();

		List<ScoreDTO> scoreList = aggregator.aggregate(results, mapIsMinus);
		
		assertEquals(2, scoreList.size());
		ScoreDTO scoreCoach2 = findByCoachId(scoreList, 2L);
		assertEquals(1, scoreCoach2.getNumberOfDraws());
		
	}

	private ScoreDTO findByCoachId(List<ScoreDTO> list, Long coachId) {
		return list.stream().filter(s -> s.getCoachId().equals(coachId)).findFirst().orElseThrow();
	}
}
