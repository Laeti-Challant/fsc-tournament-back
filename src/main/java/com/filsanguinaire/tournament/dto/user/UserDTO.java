package com.filsanguinaire.tournament.dto.user;

import com.filsanguinaire.tournament.bo.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	
	private String pseudo;
	
	private String email;
	
	private Role role;
	
	private boolean active;
}
