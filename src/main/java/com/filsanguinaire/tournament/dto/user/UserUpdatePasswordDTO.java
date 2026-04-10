package com.filsanguinaire.tournament.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordDTO {

	@NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 10, message = "Le mot de passe doit contenir au moins 10 caractères")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
        message = "Le mot de passe doit contenir au moins une majuscule et un chiffre"
    )
	private String newPassword;
}
