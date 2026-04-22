package com.filsanguinaire.tournament.dto.coach;

import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.RosterStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachAdminUpdateDTO {

	@NotNull
	private CoachStatus status;
	
	@NotNull
	private RosterStatus rosterStatus;
	
	private boolean substitute;
}
