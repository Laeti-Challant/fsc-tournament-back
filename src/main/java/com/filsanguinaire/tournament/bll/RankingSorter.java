package com.filsanguinaire.tournament.bll;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class RankingSorter {
	
	private static final Comparator<ScoreDTO> SPORTING = Comparator
			.comparingInt(ScoreDTO::getNumberOfWins)
				.thenComparingInt(ScoreDTO::getNumberOfDraws)
				.thenComparingInt(ScoreDTO::getNumberOfObjectives)
				.thenComparingInt(ScoreDTO::getNumberOfTouchdowns)
				.thenComparingInt(ScoreDTO::getNumberOfCasualties)
				.reversed();

	private static final Comparator<ScoreDTO> GENERAL = SPORTING.thenComparingLong(ScoreDTO::getCoachId);

	public List<ScoreDTO> generalSort(List<ScoreDTO> scores) {

		return scores.stream().sorted(GENERAL).toList();
	}

	public List<ScoreDTO> finalSort(List<ScoreDTO> scores) {

		List<ScoreDTO> scoresSorted = scores.stream().sorted(GENERAL).toList();

		if (!scoresSorted.isEmpty()) {
			scoresSorted.get(0).setRank(1);
		}

		for (int i = 1; i < scoresSorted.size(); i++) {
			if (SPORTING.compare(scoresSorted.get(i - 1), scoresSorted.get(i)) == 0) {
				int rank = scoresSorted.get(i - 1).getRank();
				scoresSorted.get(i).setRank(rank);

			} else {
				scoresSorted.get(i).setRank(i + 1);
			}
		}
		return scoresSorted;
	}

	public List<ScoreDTO> bashlordSort(List<ScoreDTO> scores) {
		return sortByThenGeneral(scores, ScoreDTO::getNumberOfCasualties);
	}
	
	public List<ScoreDTO> scorerSort(List<ScoreDTO> scores) {
		return sortByThenGeneral(scores, ScoreDTO::getNumberOfTouchdowns);
	}
	
	public List<ScoreDTO> passerSort(List<ScoreDTO> scores) {		
		return sortByThenGeneral(scores, ScoreDTO::getNumberOfPasses);
	}
	
	public List<ScoreDTO> foulerSort(List<ScoreDTO> scores) {
		return sortByThenGeneral(scores, ScoreDTO::getNumberOfFoulActions);
	}
	
	public List<ScoreDTO> objectiveSort(List<ScoreDTO> scores) {		
		return sortByThenGeneral(scores, ScoreDTO::getNumberOfObjectives);
	}
	
	private List<ScoreDTO> sortByThenGeneral(List<ScoreDTO> scores, ToIntFunction<ScoreDTO> criterion) {
		return scores	.stream()
						.sorted(Comparator.comparingInt(criterion).reversed().thenComparing(GENERAL))
						.toList();
	}
}