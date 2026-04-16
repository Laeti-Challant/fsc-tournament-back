package com.filsanguinaire.tournament.mapper;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bll.IRulesService;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventMapper {

	private final IRulesService rulesService; 
	
	public EventSummaryDTO toSummaryDTO(Event event) {
        return EventSummaryDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .status(event.getStatus())
                .type(event instanceof Tournament ? "TOURNAMENT" : "EVENT")
                .build();
    }

    public EventDetailDTO toDetailDTO(Event event) {
    	EventDetailDTO.EventDetailDTOBuilder builder =  EventDetailDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .eventDate(event.getEventDate())
                .registrationDeadline(event.getRegistrationDeadline())
                .status(event.getStatus())
                .maxParticipants(event.getMaxParticipants())
                .nbRounds(event.getNbRounds());
    	
                if (event instanceof Tournament tournament) {
                    builder.type("TOURNAMENT")
                           .location(tournament.getLocation())
                           .address(tournament.getAddress())
                           .postalCode(tournament.getPostalCode())
                           .city(tournament.getCity())
                           .rules(rulesService.findByTournament(tournament.getId()).orElse(null));
                } else {
                    builder.type("EVENT");
                }

                return builder.build();
    }
}
