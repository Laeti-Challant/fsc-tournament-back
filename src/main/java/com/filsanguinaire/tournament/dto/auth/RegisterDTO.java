package com.filsanguinaire.tournament.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
 
	@NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
	private String email;
 
	@NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 50, message = "Le pseudo doit contenir entre 3 et 50 caractères")
	private String pseudo;
 
	@NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 10, message = "Le mot de passe doit contenir au moins 10 caractères")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$",
        message = "Le mot de passe doit contenir au moins une majuscule et un chiffre"
    )
	private String password;
}
