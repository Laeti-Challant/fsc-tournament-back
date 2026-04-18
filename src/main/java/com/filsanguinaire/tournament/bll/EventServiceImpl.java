package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

	private final EventRepository eventRepository;	
	
	private final EventMapper mapper;
    
	@Override
	@Transactional(readOnly = true)
	public List<EventSummaryDTO> getAll() {
		return eventRepository.findAll()
                .stream()
                .map(mapper:: toSummaryDTO)
                .toList();
	}

	@Override
	@Transactional(readOnly = true)
	public EventDetailDTO getById(Long id) {
		Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return mapper.toDetailDTO(event);
	}

	
}
