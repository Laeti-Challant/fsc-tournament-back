package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.result.CoachResultDTO;
import com.filsanguinaire.tournament.dto.result.MatchResultCreateDTO;

public interface ICoachResultService {

	List<CoachResultDTO> getByMatch(Long eventId, Long roundId, Long matchId);

    List<CoachResultDTO> create(Long eventId, Long roundId, Long matchId, 
                                MatchResultCreateDTO dto);

    List<CoachResultDTO> update(Long eventId, Long roundId, Long matchId, 
                                MatchResultCreateDTO dto);
}
