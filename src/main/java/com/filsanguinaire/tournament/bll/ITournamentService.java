package com.filsanguinaire.tournament.bll;

import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.tournament.TournamentCreateUpdateDTO;

public interface ITournamentService {

	EventDetailDTO getCurrentTournament();
	
	EventDetailDTO create(TournamentCreateUpdateDTO dto);
	
	EventDetailDTO update(Long id, TournamentCreateUpdateDTO dto);
}
