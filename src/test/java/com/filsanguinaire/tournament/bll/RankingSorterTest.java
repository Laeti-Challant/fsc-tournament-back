package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class RankingSorterTest {

	
//	@BeforeEach
//	void setUp() {
//		ScoreDTO scoreDTO1 = new ScoreDTO();
//		scoreDTO1.setCoachId(1L);
//		scoreDTO1.setMinus(false);
//		scoreDTO1.setNumberOfWins(4);
//		scoreDTO1.setNumberOfDraws(1);
//		scoreDTO1.setNumberOfObjectives(10);
//		scoreDTO1.setNumberOfTouchdowns(14);
//		scoreDTO1.setNumberOfCasualties(10);
//		scoreDTO1.setNumberOfPasses(5);
//		scoreDTO1.setNumberOfFoulActions(2);
//		
//		ScoreDTO scoreDTO2 = new ScoreDTO();
//		scoreDTO2.setCoachId(2L);
//		scoreDTO2.setMinus(false);
//		scoreDTO2.setNumberOfWins(1);
//		scoreDTO2.setNumberOfDraws(3);
//		scoreDTO2.setNumberOfObjectives(15);
//		scoreDTO2.setNumberOfTouchdowns(9);
//		scoreDTO2.setNumberOfCasualties(5);
//		scoreDTO2.setNumberOfPasses(3);
//		scoreDTO2.setNumberOfFoulActions(4);
//		
//		ScoreDTO scoreDTO3 = new ScoreDTO();
//		scoreDTO3.setCoachId(3L);
//		scoreDTO3.setMinus(true);
//		scoreDTO3.setNumberOfWins(0);
//		scoreDTO3.setNumberOfDraws(0);
//		scoreDTO3.setNumberOfObjectives(5);
//		scoreDTO3.setNumberOfTouchdowns(1);
//		scoreDTO3.setNumberOfCasualties(3);
//		scoreDTO3.setNumberOfPasses(2);
//		scoreDTO3.setNumberOfFoulActions(1);
//		
//		ScoreDTO scoreDTO4 = new ScoreDTO();
//		scoreDTO4.setCoachId(4L);
//		scoreDTO4.setMinus(false);
//		scoreDTO4.setNumberOfWins(5);
//		scoreDTO4.setNumberOfDraws(0);
//		scoreDTO4.setNumberOfObjectives(20);
//		scoreDTO4.setNumberOfTouchdowns(10);
//		scoreDTO4.setNumberOfCasualties(4);
//		scoreDTO4.setNumberOfPasses(7);
//		scoreDTO4.setNumberOfFoulActions(3);
//		
//		ScoreDTO scoreDTO5 = new ScoreDTO();
//		scoreDTO5.setCoachId(5L);
//		scoreDTO5.setMinus(true);
//		scoreDTO5.setNumberOfWins(2);
//		scoreDTO5.setNumberOfDraws(2);
//		scoreDTO5.setNumberOfObjectives(9);
//		scoreDTO5.setNumberOfTouchdowns(8);
//		scoreDTO5.setNumberOfCasualties(2);
//		scoreDTO5.setNumberOfPasses(4);
//		scoreDTO5.setNumberOfFoulActions(5);
//		
//		scores = new ArrayList<ScoreDTO>();
//		scores.add(scoreDTO1);
//		scores.add(scoreDTO2);
//		scores.add(scoreDTO3);
//		scores.add(scoreDTO4);
//		scores.add(scoreDTO5);
//	}
	
	@Test
	void shouldSortByWinsDescending() {
		// Arrange
		ScoreDTO score1 = ScoreDTO.builder().coachId(1L).numberOfWins(5).build();
		ScoreDTO score2 = ScoreDTO.builder().coachId(2L).numberOfWins(3).build();
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);
		
		RankingSorter sorter = new RankingSorter();
		
		// Act
		List<ScoreDTO> sortedScores = sorter.generalSort(scores);
				
		// Assert
		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
		
	}
	
}
