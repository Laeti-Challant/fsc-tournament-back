package com.filsanguinaire.tournament.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.filsanguinaire.tournament.bll.IRankingService;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.ranking.RankingsDTO;
import com.filsanguinaire.tournament.dto.ranking.ScoreDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.security.JwtService;

@WebMvcTest(RankingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RankingControllerTest {

	@MockitoBean
	private IRankingService service;

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	private RankingsDTO ranking;

	private static final Long TOURNAMENT_ID = 1L;
	private static final Long COACH_ID = 1L;
	private static final Long UNKNOWN_TOURNAMENT_ID = 99L;

	@BeforeEach
	void setUp() {
		ScoreDTO score = ScoreDTO.builder().coachId(COACH_ID).rank(1).build();
		ranking = new RankingsDTO();
		ranking.setGeneralRanking(List.of(score));
	}

	@Test
	void shouldReturnRankingsWhenTournamentExists() throws Exception {
		// Arrange
		when(service.getRankings(TOURNAMENT_ID)).thenReturn(ranking);

		// Act
		ResultActions result = mockMvc.perform(get("/rankings/" + TOURNAMENT_ID));

		// Assert
		result	.andExpect(status().isOk())
				.andExpect(jsonPath("$.generalRanking[0].coachId").value(COACH_ID));
	}

	@Test
	void shouldReturnNotFoundWhenTournamentDoesNotExist() throws Exception {
		// Arrange
		when(service.getRankings(UNKNOWN_TOURNAMENT_ID)).thenThrow(new EventNotFoundException(UNKNOWN_TOURNAMENT_ID));
		
		// Act
		ResultActions result = mockMvc.perform(get("/rankings/" + UNKNOWN_TOURNAMENT_ID));

		// Assert
		result	.andExpect(status().isNotFound());
	}
	
	@Test
	void shouldReturnBadRequestWhenTournamentIdIsInvalid() throws Exception {
		// Act
		ResultActions result = mockMvc.perform(get("/rankings/abc"));

		// Assert
		result	.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("paramètre invalide : tournamentId"));
	}
}
