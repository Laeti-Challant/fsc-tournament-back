package com.filsanguinaire.tournament.dto.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateUpdateDTO {

	@NotBlank
	@Size(max = 50)
	private String label;
	
	@Size(max = 255)
	private String description;
	
}
