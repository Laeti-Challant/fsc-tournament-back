package com.filsanguinaire.tournament.dto.auth;

import com.filsanguinaire.tournament.bo.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

	private String pseudo;
	
	private Role role;
}
