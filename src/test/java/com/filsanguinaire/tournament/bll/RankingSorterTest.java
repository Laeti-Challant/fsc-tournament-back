package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class RankingSorterTest {

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

	@Test
	void shouldBreakTiesByDraws() {
		ScoreDTO score1 = ScoreDTO.builder().coachId(1L).numberOfWins(3).numberOfDraws(2).build();
		ScoreDTO score2 = ScoreDTO.builder().coachId(2L).numberOfWins(3).numberOfDraws(1).numberOfTouchdowns(2).build();
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> sortedScores = sorter.generalSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
	}

	@Test
	void shouldBreakTiesByObjectives() {
		ScoreDTO score1 = ScoreDTO	.builder()
									.coachId(1L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(15)
									.build();
		
		ScoreDTO score2 = ScoreDTO	.builder()
									.coachId(2L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(5)
									.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> sortedScores = sorter.generalSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
	}
	
	@Test
	void shouldBreakTiesByTouchdowns() {
		ScoreDTO score1 = ScoreDTO	.builder()
									.coachId(1L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(10)
									.numberOfTouchdowns(12)
									.build();
		
		ScoreDTO score2 = ScoreDTO	.builder()
									.coachId(2L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(10)
									.numberOfTouchdowns(7)
									.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> sortedScores = sorter.generalSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
	}
	
	@Test
	void shouldBreakTiesByCasualties() {
		ScoreDTO score1 = ScoreDTO	.builder()
									.coachId(1L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(10)
									.numberOfTouchdowns(7)
									.numberOfCasualties(5)
									.build();
		
		ScoreDTO score2 = ScoreDTO	.builder()
									.coachId(2L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(10)
									.numberOfTouchdowns(7)
									.numberOfCasualties(1)
									.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> sortedScores = sorter.generalSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
	}
}
