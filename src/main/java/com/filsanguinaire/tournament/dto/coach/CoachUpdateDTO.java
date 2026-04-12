package com.filsanguinaire.tournament.dto.coach;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachUpdateDTO {

	@NotBlank
	private String coachPseudo;
	
	@NotBlank
	private String teamName;
	
	@NotBlank
	private String race;
	
	private boolean eating;
	
	private boolean vegetarian;
}
