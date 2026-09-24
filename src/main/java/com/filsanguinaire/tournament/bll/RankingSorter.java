package com.filsanguinaire.tournament.bll;

import java.util.Comparator;
import java.util.List;

import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class RankingSorter {
	
	private static final Comparator<ScoreDTO> GENERAL = Comparator
			.comparingInt(ScoreDTO::getNumberOfWins)
				.thenComparingInt(ScoreDTO::getNumberOfDraws)
				.thenComparingInt(ScoreDTO::getNumberOfObjectives)
				.thenComparingInt(ScoreDTO::getNumberOfTouchdowns)
				.thenComparingInt(ScoreDTO::getNumberOfCasualties)
				.reversed();

	public List<ScoreDTO> generalSort(List<ScoreDTO> scores) {		

		return scores.stream().sorted(GENERAL).toList();
	}
}
