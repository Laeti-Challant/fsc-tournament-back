package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.round.RoundDTO;
import com.filsanguinaire.tournament.dto.round.RoundUpdateDTO;

public interface IRoundService {

	List<RoundDTO> findAllByEvent(Long eventId);
	
    RoundDTO findByIdAndEvent(Long id, Long eventId);
    
    List<RoundDTO> generateAll(Long eventId);
    
    RoundDTO update(Long id, Long eventId, RoundUpdateDTO dto);
}
