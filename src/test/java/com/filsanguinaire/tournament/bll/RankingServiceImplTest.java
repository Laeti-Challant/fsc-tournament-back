package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.CoachResultRepository;
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
	
	@InjectMocks
	private RankingServiceImpl rankingService;
	
	@Test
	void shouldReturnRankingsTournament() {
		// Arrange
		
		// tournoi
		Long tournamentId = 1L;
		
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
		when(tournamentRulesRepo.findByTournamentId(tournamentId)).thenReturn(opt);		
		when(coachResultRepo.findAllByMatch_Round_Event_Id(tournamentId)).thenReturn(List.of(coachResult1));

		// Act		
		RankingsDTO rankings = rankingService.getRankings(tournamentId);
		
		// Assert
		assertEquals(1, rankings.getGeneralRanking().size());
		assertEquals(1L, rankings.getGeneralRanking().get(0).getCoachId());
		assertEquals(1, rankings.getGeneralRanking().get(0).getNumberOfWins());		
	}
	
}
