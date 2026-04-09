package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dto.event.EventCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

	private final EventRepository eventRepository;
	private final IRulesService rulesService;
    
	@Override
	@Transactional(readOnly = true)
	public List<EventSummaryDTO> getAll() {
		return eventRepository.findAll()
                .stream()
                .map(this::toSummaryDTO)
                .toList();
	}

	@Override
	@Transactional(readOnly = true)
	public EventDetailDTO getCurrent() {
		Event event = eventRepository
                .findFirstByStatusIn(List.of(EventStatus.PLANNED, EventStatus.IN_PROGRESS))
                .orElseThrow(() -> new EventNotFoundException(0L));
        return toDetailDTO(event);
	}

	@Override
	@Transactional(readOnly = true)
	public EventDetailDTO getById(Long id) {
		Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return toDetailDTO(event);
	}

	@Override
	@Transactional
	public EventDetailDTO create(EventCreateUpdateDTO dto) {
		Event event = Event.builder()
                .name(dto.getName())
                .eventDate(dto.getEventDate())
                .registrationDeadline(dto.getRegistrationDeadline())
                .maxParticipants(dto.getMaxParticipants())
                .nbRounds(dto.getNbRounds())
                .status(EventStatus.PLANNED)
                .build();
        return toDetailDTO(eventRepository.save(event));
	}

	@Override
	@Transactional
	public EventDetailDTO update(Long id, EventCreateUpdateDTO dto) {
		Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        event.setName(dto.getName());
        event.setEventDate(dto.getEventDate());
        event.setRegistrationDeadline(dto.getRegistrationDeadline());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setNbRounds(dto.getNbRounds());
        return toDetailDTO(eventRepository.save(event));
	}

	private EventSummaryDTO toSummaryDTO(Event event) {
        return EventSummaryDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .status(event.getStatus())
                .build();
    }

    private EventDetailDTO toDetailDTO(Event event) {
    	return EventDetailDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .registrationDeadline(event.getRegistrationDeadline())
                .status(event.getStatus())
                .maxParticipants(event.getMaxParticipants())
                .nbRounds(event.getNbRounds())
                .rules(rulesService.findByEvent(event.getId()).orElse(null)) 
                .build();
    }
}
