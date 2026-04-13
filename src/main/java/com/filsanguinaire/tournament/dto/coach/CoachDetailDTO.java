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
public class CoachDetailDTO {

	private Long id;
	
	private String coachPseudo;
	
	private String teamName;
	
	private String race;
	
	private boolean eating;
	
	private boolean vegetarian;
	
	private CoachStatus status;
	
	private RosterStatus rosterStatus;
	
	private Long userId;
	
	private String userPseudo;
	
	private String userEmail;
}
