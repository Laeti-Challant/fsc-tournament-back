package com.filsanguinaire.tournament.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
 
	private String email;
 
	private String pseudo;
 
	private String password;
}
