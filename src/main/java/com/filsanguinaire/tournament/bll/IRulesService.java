package com.filsanguinaire.tournament.bll;

import java.util.Optional;

import com.filsanguinaire.tournament.dto.rules.TournamentRulesCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;

public interface IRulesService {
	
	Optional<TournamentRulesDTO> findByTournament(Long tournamentId);

	TournamentRulesDTO getByTournament(Long tournamentId);

    TournamentRulesDTO create(Long tournamentId, TournamentRulesCreateUpdateDTO dto);

    TournamentRulesDTO update(Long tournamentId, TournamentRulesCreateUpdateDTO dto);

    TournamentRulesDTO cloneFromTournament(Long targetTournamentId, Long sourceTournamentId);
}
