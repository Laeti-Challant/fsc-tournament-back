package com.filsanguinaire.tournament.dto.match;

import com.filsanguinaire.tournament.bo.MatchStatus;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {

	private Long id;
	
    private MatchStatus status;
    
    private CoachSummaryDTO coach1;
    
    private CoachSummaryDTO coach2;
}
