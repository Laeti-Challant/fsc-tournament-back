package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.event.EventCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;

public interface IEventService {

	List<EventSummaryDTO> getAll();

    EventDetailDTO getCurrent();

    EventDetailDTO getById(Long id);

    EventDetailDTO create(EventCreateUpdateDTO dto);

    EventDetailDTO update(Long id, EventCreateUpdateDTO dto);
}
