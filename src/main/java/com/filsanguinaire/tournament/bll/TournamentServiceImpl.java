package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.tournament.TournamentCreateUpdateDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements ITournamentService {

	private final TournamentRepository tournamentRepository;
	    
    private final EventMapper mapper;
    
	@Override
	public EventDetailDTO getCurrentTournament() {
		Tournament tournament = tournamentRepository
	            .findFirstByStatusIn(List.of(EventStatus.PLANNED, EventStatus.IN_PROGRESS))
	            .orElseThrow(() -> new EventNotFoundException(0L));
	    return mapper.toDetailDTO(tournament);
	}

	@Override
	public EventDetailDTO create(TournamentCreateUpdateDTO dto) {
		Tournament tournament = Tournament.builder()
                .name(dto.getName())
                .eventDate(dto.getEventDate())
                .registrationDeadline(dto.getRegistrationDeadline())
                .maxParticipants(dto.getMaxParticipants())
                .nbRounds(dto.getNbRounds())
                .status(EventStatus.PLANNED)
                .location(dto.getLocation())
                .address(dto.getAddress())
                .postalCode(dto.getPostalCode())
                .city(dto.getCity())
                .build();

        Tournament saved = tournamentRepository.save(tournament);
        return mapper.toDetailDTO(saved);
	}

	@Override
	public EventDetailDTO update(Long id, TournamentCreateUpdateDTO dto) {
		Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
		
		tournament.setName(dto.getName());
	    tournament.setEventDate(dto.getEventDate());
	    tournament.setRegistrationDeadline(dto.getRegistrationDeadline());
	    tournament.setMaxParticipants(dto.getMaxParticipants());
	    tournament.setNbRounds(dto.getNbRounds());
	    tournament.setLocation(dto.getLocation());
	    tournament.setAddress(dto.getAddress());
	    tournament.setPostalCode(dto.getPostalCode());
	    tournament.setCity(dto.getCity());	    
	    
	    Tournament saved = tournamentRepository.save(tournament);
	    return mapper.toDetailDTO(saved);
	}

}
