package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.tournament.TournamentCreateUpdateDTO;

public interface ITournamentService {

	EventDetailDTO getCurrentTournament();
	
	List<EventDetailDTO> getActiveTournaments();
	
	EventDetailDTO create(TournamentCreateUpdateDTO dto);
	
	EventDetailDTO update(Long id, TournamentCreateUpdateDTO dto);	
}
