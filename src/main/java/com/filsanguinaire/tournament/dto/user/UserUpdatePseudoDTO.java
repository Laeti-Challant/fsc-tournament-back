package com.filsanguinaire.tournament.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePseudoDTO {

	@NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 50, message = "Le pseudo doit contenir entre 3 et 50 caractères")
	private String pseudo;
}
