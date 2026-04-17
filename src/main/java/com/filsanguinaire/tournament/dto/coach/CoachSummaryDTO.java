package com.filsanguinaire.tournament.dto.coach;

import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.RosterStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachSummaryDTO {

	private Long id;
	
    private String coachPseudo;
    
    private String teamName;
    
    private String race;
    
    private CoachStatus status;
    
    private RosterStatus rosterStatus;
}
