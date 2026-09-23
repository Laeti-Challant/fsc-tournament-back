package com.filsanguinaire.tournament.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {

	private Long coachId;
	
	private boolean isMinus;
	
	private int numberOfWins;
	
	private int numberOfDraws;
	
	private int numberOfObjectives;
	
	private int numberOfTouchdowns;
	
	private int numberOfCasualties;
	
	private int numberOfPasses;
	
	private int numberOfFoulActions;
}
