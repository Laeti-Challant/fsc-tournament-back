package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.match.MatchDTO;

public interface IPairingsService {

	List<MatchDTO> generatepairings(Long eventId, Long roundId);
}
