package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	
	@Test
	void shouldBreakTiesByCoachId() {
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
									.numberOfCasualties(5)
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
	void shouldShareRankOnPerfectTieAndSkipNext() {
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
									.numberOfCasualties(5)
									.build();
		
		ScoreDTO score3 = ScoreDTO	.builder()
									.coachId(3L)
									.numberOfWins(5)
									.numberOfDraws(0)
									.numberOfObjectives(15)
									.numberOfTouchdowns(18)
									.numberOfCasualties(6)
									.build();
		
		ScoreDTO score4 = ScoreDTO	.builder()
									.coachId(4L)
									.numberOfWins(1)
									.numberOfDraws(2)
									.numberOfObjectives(5)
									.numberOfTouchdowns(6)
									.numberOfCasualties(2)
									.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);
		scores.add(score3);
		scores.add(score4);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> finalScores = sorter.finalSort(scores);

		assertEquals(3L, finalScores.get(0).getCoachId());
		assertEquals(1L, finalScores.get(1).getCoachId());
		assertEquals(2L, finalScores.get(2).getCoachId());
		assertEquals(4L, finalScores.get(3).getCoachId());
		
		assertEquals(1, finalScores.get(0).getRank());
		assertEquals(2, finalScores.get(1).getRank());
		assertEquals(2, finalScores.get(2).getRank());
		assertEquals(4, finalScores.get(3).getRank());
	}
	
	@Test
	void shouldReturnEmptyListWhenNoScores() {
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		
		RankingSorter sorter = new RankingSorter();
		
		List<ScoreDTO> finalScores = sorter.finalSort(scores);
		
		assertTrue(finalScores.isEmpty());
	}
	
	@Test
	void shouldPutCoachWithMostCasualtiesFirst () {
		ScoreDTO score1 = ScoreDTO	.builder()
									.coachId(1L)
									.numberOfWins(1)
									.numberOfDraws(3)
									.numberOfObjectives(10)
									.numberOfTouchdowns(7)
									.numberOfCasualties(20)
									.build();
		
		ScoreDTO score2 = ScoreDTO	.builder()
									.coachId(2L)
									.numberOfWins(3)
									.numberOfDraws(1)
									.numberOfObjectives(10)
									.numberOfTouchdowns(7)
									.numberOfCasualties(5)
									.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);

		RankingSorter sorter = new RankingSorter();

		List<ScoreDTO> sortedScores = sorter.bashlordSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());
	}
	
	@Test
	void shouldBreakBashlordTiesByGeneralRanking() {
		ScoreDTO score1 = ScoreDTO	.builder()
						.coachId(1L)
						.numberOfWins(1)
						.numberOfDraws(3)
						.numberOfObjectives(10)
						.numberOfTouchdowns(7)
						.numberOfCasualties(20)
						.build();

		ScoreDTO score2 = ScoreDTO	.builder()
						.coachId(2L)
						.numberOfWins(3)
						.numberOfDraws(1)
						.numberOfObjectives(10)
						.numberOfTouchdowns(7)
						.numberOfCasualties(5)
						.build();
		ScoreDTO score3 = ScoreDTO	.builder()
						.coachId(3L)
						.numberOfWins(3)
						.numberOfDraws(0)
						.numberOfObjectives(12)
						.numberOfTouchdowns(10)
						.numberOfCasualties(20)
						.build();
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);
		scores.add(score3);
		
		RankingSorter sorter = new RankingSorter();
		
		List<ScoreDTO> sortedScores = sorter.bashlordSort(scores);
		
		assertEquals(3L, sortedScores.get(0).getCoachId());
		assertEquals(1L, sortedScores.get(1).getCoachId());
		assertEquals(2L, sortedScores.get(2).getCoachId());
	}
	
	@Test
	void shouldPutCoachWithMostTouchdownsFirst() {
		ScoreDTO score1 = ScoreDTO	.builder()
						.coachId(1L)
						.numberOfWins(2)
						.numberOfDraws(2)
						.numberOfObjectives(10)
						.numberOfTouchdowns(15)
						.numberOfCasualties(10)
						.build();

		ScoreDTO score2 = ScoreDTO	.builder()
						.coachId(2L)
						.numberOfWins(3)
						.numberOfDraws(1)
						.numberOfObjectives(10)
						.numberOfTouchdowns(10)
						.numberOfCasualties(10)
						.build();		
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);
		
		RankingSorter sorter = new RankingSorter();
		
		List<ScoreDTO> sortedScores = sorter.scorerSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(2L, sortedScores.get(1).getCoachId());	
	}
	
	@Test
	void shouldBreakScorerTiesByGeneralRanking() {
		ScoreDTO score1 = ScoreDTO	.builder()
						.coachId(1L)
						.numberOfWins(2)
						.numberOfDraws(2)
						.numberOfObjectives(10)
						.numberOfTouchdowns(15)
						.numberOfCasualties(10)
						.build();

		ScoreDTO score2 = ScoreDTO	.builder()
						.coachId(2L)
						.numberOfWins(3)
						.numberOfDraws(1)
						.numberOfObjectives(10)
						.numberOfTouchdowns(10)
						.numberOfCasualties(10)
						.build();
		
		ScoreDTO score3 = ScoreDTO	.builder()
						.coachId(3L)
						.numberOfWins(5)
						.numberOfDraws(0)
						.numberOfObjectives(10)
						.numberOfTouchdowns(10)
						.numberOfCasualties(10)
						.build();	
		
		List<ScoreDTO> scores = new ArrayList<ScoreDTO>();
		scores.add(score2);
		scores.add(score1);
		scores.add(score3);
		
		RankingSorter sorter = new RankingSorter();
		
		List<ScoreDTO> sortedScores = sorter.scorerSort(scores);

		assertEquals(1L, sortedScores.get(0).getCoachId());
		assertEquals(3L, sortedScores.get(1).getCoachId());
		assertEquals(2L, sortedScores.get(2).getCoachId());
	}
}
