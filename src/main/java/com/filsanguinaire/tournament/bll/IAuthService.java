package com.filsanguinaire.tournament.bll;

import com.filsanguinaire.tournament.dto.auth.AuthResponseDTO;
import com.filsanguinaire.tournament.dto.auth.LoginDTO;
import com.filsanguinaire.tournament.dto.auth.RegisterDTO;

import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

	AuthResponseDTO register(RegisterDTO dto, HttpServletResponse response);
	
	AuthResponseDTO login(LoginDTO dto, HttpServletResponse response);
	
	void logout(HttpServletResponse response);
}
