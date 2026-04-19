package com.filsanguinaire.tournament.dto.coach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachParticipantDTO {

	private Long id;
	
    private String coachPseudo;
    
    private String teamName;
    
    private String race;
}
