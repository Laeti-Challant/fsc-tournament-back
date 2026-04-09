package com.filsanguinaire.tournament.bll;

import java.util.Optional;

import com.filsanguinaire.tournament.dto.rules.TournamentRulesCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;

public interface IRulesService {
	
	Optional<TournamentRulesDTO> findByEvent(Long eventId);

	TournamentRulesDTO getByEvent(Long eventId);

    TournamentRulesDTO create(Long eventId, TournamentRulesCreateUpdateDTO dto);

    TournamentRulesDTO update(Long eventId, TournamentRulesCreateUpdateDTO dto);

    TournamentRulesDTO cloneFromEvent(Long targetEventId, Long sourceEventId);
}
