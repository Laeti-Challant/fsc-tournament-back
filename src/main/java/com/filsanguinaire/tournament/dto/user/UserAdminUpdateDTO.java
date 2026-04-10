package com.filsanguinaire.tournament.dto.user;

import com.filsanguinaire.tournament.bo.Role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminUpdateDTO {

	@NotNull(message = "Le rôle est obligatoire")
	private Role role;
	
	private boolean active;
}
