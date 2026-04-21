package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.match.MatchDTO;

public interface IPairingService {

	List<MatchDTO> generatePairings(Long eventId, Long roundId);
}
