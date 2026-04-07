package com.filsanguinaire.tournament.dto.user;

import com.filsanguinaire.tournament.bo.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminUpdateDTO {

	private Role role;
	
	private boolean active;
}
