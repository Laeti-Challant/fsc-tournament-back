package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.match.MatchCreateDTO;
import com.filsanguinaire.tournament.dto.match.MatchDTO;

public interface IMatchService {

	List<MatchDTO> findAllByRound(Long eventId, Long roundId);
	
    MatchDTO findByIdAndRound(Long id, Long roundId, Long eventId);    
    
    MatchDTO createChallenge(Long eventId, Long roundId, MatchCreateDTO dto);
}
