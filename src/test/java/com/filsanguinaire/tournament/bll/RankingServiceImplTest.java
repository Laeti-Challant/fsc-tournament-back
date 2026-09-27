package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.bo.RosterCategory;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.bo.RoundStatus;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.CoachResultRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dto.ranking.RankingsDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RankingServiceImplTest {

	@Mock
	private CoachResultRepository coachResultRepo;
	
	@Mock
	private CoachRepository coachRepo;
	
	@Mock
	private TournamentRulesRepository tournamentRulesRepo;
	
	@Mock
	private TournamentRepository tournamentRepo;
	
	@Mock
	private RoundRepository roundRepo;
	
	@InjectMocks
	private RankingServiceImpl rankingService;
	
	private static final Long TOURNAMENT_ID = 1L;
	private static final String ORCS = "Orcs";
	private static final String GOB = "Gobelins";
	
	@Test
	void shouldReturnRankingsTournament() {
		// Arrange		
		Coach coach = coach(1L, ORCS);
		
		// Créée le coachresult
		CoachResult coachResult1 = coachResult(coach, MatchResult.WIN);		
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		TournamentRules rules = rules(List.of(rosterCategory(ORCS, false)));
		// stubs
		when(tournamentRepo.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament()));		
		when(tournamentRulesRepo.findByTournamentId(TOURNAMENT_ID)).thenReturn(Optional.of(rules));		
		when(coachResultRepo.findAllByMatch_Round_Event_Id(TOURNAMENT_ID)).thenReturn(List.of(coachResult1));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(TOURNAMENT_ID);
		
		// Assert
		assertEquals(1, rankings.getGeneralRanking().size());
		assertEquals(1L, rankings.getGeneralRanking().get(0).getCoachId());
		assertEquals(1, rankings.getGeneralRanking().get(0).getNumberOfWins());		
	}
	
	@Test
	void shouldIncludeValidatedCoachWithoutResult() {
		// Arrange				
		Coach coach1 = coach(1L, ORCS);		
		Coach coach2 = coach(2L, ORCS);
		
		CoachResult coachResult1 = coachResult(coach1, MatchResult.WIN);		
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		TournamentRules rules = rules(List.of(rosterCategory(ORCS, false)));
		// stubs
		when(tournamentRepo.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament()));
		when(tournamentRulesRepo.findByTournamentId(TOURNAMENT_ID)).thenReturn(Optional.of(rules));
		when(coachRepo.findByEventIdAndStatus(TOURNAMENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2));
		when(coachResultRepo.findAllByMatch_Round_Event_Id(TOURNAMENT_ID)).thenReturn(List.of(coachResult1));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(TOURNAMENT_ID);
		
		// Assert
		assertEquals(2, rankings.getGeneralRanking().size());
		assertEquals(2L, rankings.getGeneralRanking().get(1).getCoachId());
		assertEquals(0, rankings.getGeneralRanking().get(1).getNumberOfWins());	
	}
	
	@Test
	void shouldReturnGeneralWithoutRankWhenTournamentInProgress() {
		// Arrange							
		Coach coach = coach(1L, ORCS);		
		
		CoachResult coachResult1 = coachResult(coach, MatchResult.WIN);		
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		TournamentRules rules = rules(List.of(rosterCategory(ORCS, false)));
		
		when(tournamentRepo.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament()));
		when(roundRepo.findByEventIdOrderByRoundNumberAsc(TOURNAMENT_ID)).thenReturn(List.of(round(1, RoundStatus.FINISHED), round(2, RoundStatus.IN_PROGRESS)));
		when(tournamentRulesRepo.findByTournamentId(TOURNAMENT_ID)).thenReturn(Optional.of(rules));		
		when(coachResultRepo.findAllByMatch_Round_Event_Id(TOURNAMENT_ID)).thenReturn(List.of(coachResult1));
		
		// Act
		RankingsDTO rankings = rankingService.getRankings(TOURNAMENT_ID);
		
		// Assert
		assertEquals(0, rankings.getGeneralRanking().get(0).getRank());
		assertNull(rankings.getBashlordRanking(), "Ce classement ne doit pas exister");
	}
	
	@Test
	void shouldReturnFinalRankingsWhenTournamentFinished() {
		// Arrange						
		Coach coach1 = coach(1L, ORCS);		
		Coach coach2 = coach(2L, GOB);
		
		CoachResult coachResult1 = coachResult(coach1, MatchResult.WIN);		
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		CoachResult coachResult2 = coachResult(coach2, MatchResult.LOSS);		
		coachResult2.setTouchdowns(5);
		coachResult2.setCasualties(5);
		coachResult2.setObjectives(5);
		coachResult2.setBonusObjective(true);
		coachResult2.setPasses(10);
		coachResult2.setFoulActions(10);
		
		TournamentRules rules = rules(List.of(rosterCategory(ORCS, false), rosterCategory(GOB, true)));
		// stubs
		when(tournamentRepo.findById(TOURNAMENT_ID)).thenReturn(Optional.of(tournament()));
		when(roundRepo.findByEventIdOrderByRoundNumberAsc(TOURNAMENT_ID)).thenReturn(List.of(round(1, RoundStatus.FINISHED), round(2, RoundStatus.FINISHED)));
		when(tournamentRulesRepo.findByTournamentId(TOURNAMENT_ID)).thenReturn(Optional.of(rules));
		when(coachRepo.findByEventIdAndStatus(TOURNAMENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2));
		when(coachResultRepo.findAllByMatch_Round_Event_Id(TOURNAMENT_ID)).thenReturn(List.of(coachResult1, coachResult2));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(TOURNAMENT_ID);
		
		// Assert
		assertEquals(1, rankings.getGeneralRanking().get(0).getRank());;
		assertEquals(1L, rankings.getGeneralRanking().get(0).getCoachId());
		assertEquals(2L, rankings.getBashlordRanking().get(0).getCoachId());
		assertEquals(2L, rankings.getMinusRanking().get(0).getCoachId());
		assertEquals(1, rankings.getMinusRanking().size());
		assertEquals(2L, rankings.getScorerRanking().get(0).getCoachId());
		assertEquals(2L, rankings.getObjectiveRanking().get(0).getCoachId());
		assertEquals(2L, rankings.getPasserRanking().get(0).getCoachId());
		assertEquals(2L, rankings.getFoulerRanking().get(0).getCoachId());		
	}
	
	@Test
	void shouldThrowExceptionWhenTournamentNotFound() {
		when(tournamentRepo.findById(TOURNAMENT_ID)).thenReturn(Optional.empty());
		
		assertThrows(EventNotFoundException.class, () -> rankingService.getRankings(TOURNAMENT_ID));
	}
	
	private Tournament tournament() {
		Tournament tournament = new Tournament();
		tournament.setId(TOURNAMENT_ID);
		tournament.setNbRounds(2);
		return tournament;
	}
	
	private Round round(int number, RoundStatus status) {
	    Round round = new Round();
	    round.setRoundNumber(number);
	    round.setStatus(status);
	    return round;
	}
	
	private RosterCategory rosterCategory(String race, boolean isMinus) {
		RosterCategory rosterCategory = new RosterCategory();
		rosterCategory.setRaceName(race);
		rosterCategory.setMinus(isMinus);
		return rosterCategory;
	}
	
	private TournamentRules rules(List<RosterCategory> rosterCategories) {
		TournamentRules rules = new TournamentRules();
		rules.setRosterCategories(rosterCategories);
		return rules;
	}
	
	private Coach coach(Long id, String race) {
		Coach coach = new Coach();
		coach.setId(id);
		coach.setRace(race);
		coach.setStatus(CoachStatus.VALIDATED);
		return coach;
	}
	
	private CoachResult coachResult(Coach coach, MatchResult result) {
		CoachResult coachResult = new CoachResult();
		coachResult.setCoach(coach);
		coachResult.setResult(result);
		return coachResult;
	}
}
