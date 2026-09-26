package com.filsanguinaire.tournament.dto.ranking;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingsDTO {

	private List<ScoreDTO> generalRanking;
	
	private List<ScoreDTO> bashlordRanking;
	
	private List<ScoreDTO> scorerRanking;
	
	private List<ScoreDTO> passerRanking;
	
	private List<ScoreDTO> foulerRanking;
	
	private List<ScoreDTO> objectiveRanking;
	
	private List<ScoreDTO> minusRanking;
}
