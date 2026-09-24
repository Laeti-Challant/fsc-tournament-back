package com.filsanguinaire.tournament.bll;

import java.util.Comparator;
import java.util.List;

import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;

public class RankingSorter {

	public List<ScoreDTO> generalSort(List<ScoreDTO> scores) {
		
		Comparator<ScoreDTO> byWins = Comparator.comparingInt(ScoreDTO::getNumberOfWins).reversed();
		
		return scores.stream().sorted(byWins).toList();
		 
	}
}
