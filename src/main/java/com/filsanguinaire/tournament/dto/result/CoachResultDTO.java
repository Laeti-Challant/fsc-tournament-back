package com.filsanguinaire.tournament.dto.result;

import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachResultDTO {

	private Long id;
	
    private MatchResult result;
    
    private int touchdowns;
    
    private int casualties;
    
    private int objectives;
    
    private boolean bonusObjective;
    
    private int passes;
    
    private int foulActions;
    
    private CoachSummaryDTO coach;
}
