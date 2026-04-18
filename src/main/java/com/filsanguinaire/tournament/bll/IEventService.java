package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;

public interface IEventService {

	List<EventSummaryDTO> getAll();

    EventDetailDTO getById(Long id);    
}
