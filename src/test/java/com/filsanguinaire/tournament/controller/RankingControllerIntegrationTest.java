package com.filsanguinaire.tournament.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.filsanguinaire.tournament.TestcontainersConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
public class RankingControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldAllowAnonymousAccessToRankings() throws Exception {
		// Act
		ResultActions result = mockMvc.perform(get("/rankings/99"));
		
		// Assert
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	void shouldRejectAnonymousWriteOnRankings() throws Exception {
		// Act
		ResultActions result = mockMvc.perform(post("/rankings/1"));
		
		// Assert
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	void shouldReturnBadRequestWithoutTechnicalDetailsWhenIdIsInvalid() throws Exception {
		// Act
		ResultActions result = mockMvc.perform(get("/rankings/abc"));
		
		// Assert
		result	.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("paramètre invalide : tournamentId"));
	}
}
