package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
	
	@Test
	void shouldReturnRankingsTournament() {
		// Arrange
		
		// tournoi
		Tournament tournament = new Tournament();
		tournament.setId(1L);
		tournament.setNbRounds(2);
		Optional<Tournament> optTournament = Optional.of(tournament);
		
		// Règles du tournoi
		RosterCategory rosterCategory = new RosterCategory();
		rosterCategory.setId(1L);
		rosterCategory.setRaceName("Orcs");
		rosterCategory.setMinus(false);
		
		List<RosterCategory> rosterList = new ArrayList<RosterCategory>();
		rosterList.add(rosterCategory);
		
		TournamentRules tournamentRules = new TournamentRules();
		tournamentRules.setRosterCategories(rosterList);
		Optional<TournamentRules> opt = Optional.of(tournamentRules);
		
		// Créé le coach
		Coach coach = new Coach();
		coach.setId(1L);		
		coach.setRace("Orcs");
		coach.setStatus(CoachStatus.VALIDATED);
		
		// Créée le coachresult
		CoachResult coachResult1 = new CoachResult();
		coachResult1.setId(1L);
		coachResult1.setCoach(coach);
		coachResult1.setResult(MatchResult.WIN);
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		// stubs
		when(tournamentRepo.findById(tournament.getId())).thenReturn(optTournament);		
		when(tournamentRulesRepo.findByTournamentId(tournament.getId())).thenReturn(opt);		
		when(coachResultRepo.findAllByMatch_Round_Event_Id(tournament.getId())).thenReturn(List.of(coachResult1));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(tournament.getId());
		
		// Assert
		assertEquals(1, rankings.getGeneralRanking().size());
		assertEquals(1L, rankings.getGeneralRanking().get(0).getCoachId());
		assertEquals(1, rankings.getGeneralRanking().get(0).getNumberOfWins());		
	}
	
	@Test
	void shouldIncludeValidatedCoachWithoutResult() {
		// Arrange
		Tournament tournament = new Tournament();
		tournament.setId(1L);
		tournament.setNbRounds(2);
		Optional<Tournament> optTournament = Optional.of(tournament);
		
		RosterCategory rosterCategory = new RosterCategory();
		rosterCategory.setId(1L);
		rosterCategory.setRaceName("Orcs");
		rosterCategory.setMinus(false);
		
		List<RosterCategory> rosterList = new ArrayList<RosterCategory>();
		rosterList.add(rosterCategory);
		
		TournamentRules tournamentRules = new TournamentRules();
		tournamentRules.setRosterCategories(rosterList);
		Optional<TournamentRules> opt = Optional.of(tournamentRules);
		
		Coach coach1 = new Coach();
		coach1.setId(1L);		
		coach1.setRace("Orcs");
		coach1.setStatus(CoachStatus.VALIDATED);
		Coach coach2 = new Coach();
		coach2.setId(2L);
		coach2.setRace("Orcs");
		coach2.setStatus(CoachStatus.VALIDATED);

		CoachResult coachResult1 = new CoachResult();
		coachResult1.setId(1L);
		coachResult1.setCoach(coach1);
		coachResult1.setResult(MatchResult.WIN);
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		// stubs
		when(tournamentRepo.findById(tournament.getId())).thenReturn(optTournament);
		when(tournamentRulesRepo.findByTournamentId(tournament.getId())).thenReturn(opt);
		when(coachRepo.findByEventIdAndStatus(tournament.getId(), CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2));
		when(coachResultRepo.findAllByMatch_Round_Event_Id(tournament.getId())).thenReturn(List.of(coachResult1));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(tournament.getId());
		
		// Assert
		assertEquals(2, rankings.getGeneralRanking().size());
		assertEquals(2L, rankings.getGeneralRanking().get(1).getCoachId());
		assertEquals(0, rankings.getGeneralRanking().get(1).getNumberOfWins());	
	}
	
	@Test
	void shouldReturnGeneralWithoutRankWhenTournamentInProgress() {
		// Arrange
		Tournament tournament = new Tournament();
		tournament.setId(1L);
		tournament.setNbRounds(2);
		Optional<Tournament> optTournament = Optional.of(tournament);
		
		Round round1 = new Round();
		round1.setEvent(tournament);
		round1.setId(1L);
		round1.setRoundNumber(1);
		round1.setStatus(RoundStatus.FINISHED);
		Round round2 = new Round();
		round2.setEvent(tournament);
		round2.setId(2L);
		round2.setRoundNumber(2);
		round2.setStatus(RoundStatus.IN_PROGRESS);
		List<Round> rounds = new ArrayList<Round>();
		rounds.add(round1);
		rounds.add(round2);
		
		RosterCategory rosterCategory = new RosterCategory();
		rosterCategory.setId(1L);
		rosterCategory.setRaceName("Orcs");
		rosterCategory.setMinus(false);
		
		List<RosterCategory> rosterList = new ArrayList<RosterCategory>();
		rosterList.add(rosterCategory);
		
		TournamentRules tournamentRules = new TournamentRules();
		tournamentRules.setRosterCategories(rosterList);
		Optional<TournamentRules> optRules = Optional.of(tournamentRules);
		
		Coach coach = new Coach();
		coach.setId(1L);		
		coach.setRace("Orcs");
		coach.setStatus(CoachStatus.VALIDATED);
		
		CoachResult coachResult1 = new CoachResult();
		coachResult1.setId(1L);
		coachResult1.setCoach(coach);
		coachResult1.setResult(MatchResult.WIN);
		coachResult1.setTouchdowns(2);
		coachResult1.setCasualties(1);
		coachResult1.setObjectives(2);
		coachResult1.setBonusObjective(true);
		coachResult1.setPasses(2);
		coachResult1.setFoulActions(2);
		
		when(tournamentRepo.findById(tournament.getId())).thenReturn(optTournament);
		when(roundRepo.findByEventIdOrderByRoundNumberAsc(tournament.getId())).thenReturn(rounds);
		when(tournamentRulesRepo.findByTournamentId(tournament.getId())).thenReturn(optRules);		
		when(coachResultRepo.findAllByMatch_Round_Event_Id(tournament.getId())).thenReturn(List.of(coachResult1));
		
		// Act
		RankingsDTO rankings = rankingService.getRankings(tournament.getId());
		
		// Assert
		assertEquals(0, rankings.getGeneralRanking().get(0).getRank());
		assertNull(rankings.getBashlordRanking(), "Ce classement ne doit pas exister");
	}
}
