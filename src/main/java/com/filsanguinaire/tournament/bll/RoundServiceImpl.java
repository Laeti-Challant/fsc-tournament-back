package com.filsanguinaire.tournament.bll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.PairingType;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.dto.round.RoundDTO;
import com.filsanguinaire.tournament.dto.round.RoundUpdateDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.RoundNotFoundException;
import com.filsanguinaire.tournament.exceptions.RoundsAlreadyGeneratedException;
import com.filsanguinaire.tournament.exceptions.TournamentNotEditableException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements IRoundService {

	private final RoundRepository roundRepository;
	
	private final EventRepository eventRepository;
	
	@Override
	public List<RoundDTO> findAllByEvent(Long eventId) {
		if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return roundRepository.findByEventIdOrderByRoundNumberAsc(eventId)
                .stream()
                .map(this::toDTO)
                .toList();
	}

	@Override
	public RoundDTO findByIdAndEvent(Long id, Long eventId) {
		Round round = roundRepository.findByIdAndEventId(id, eventId)
                .orElseThrow(() -> new RoundNotFoundException(
                    id, eventId));
        return toDTO(round);
	}

	@Override
	@Transactional
	public List<RoundDTO> generateAll(Long eventId) {
	    Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new EventNotFoundException(eventId));

	 // Génération impossible si le tournoi est déjà en cours ou terminé
	    if (event.getStatus() == EventStatus.IN_PROGRESS 
	            || event.getStatus() == EventStatus.FINISHED) {
	        throw new TournamentNotEditableException();
	    }
	    
	    // Vérifie qu'aucun round n'existe déjà pour cet event
	    if (!roundRepository.findByEventIdOrderByRoundNumberAsc(eventId).isEmpty()) {
	        throw new RoundsAlreadyGeneratedException(eventId);
	    }

	    List<Round> rounds = new ArrayList<>();
	    for (int i = 1; i <= event.getNbRounds(); i++) {
	        rounds.add(Round.builder()
	                .roundNumber(i)
	                // Round 1 toujours aléatoire, suivants en Swiss
	                .pairingType(i == 1 ? PairingType.RANDOM : PairingType.SWISS)
	                .event(event)
	                .build());
	    }

	    return roundRepository.saveAll(rounds)
	            .stream()
	            .map(this::toDTO)
	            .toList();
	}

	@Override
	public RoundDTO update(Long id, Long eventId, RoundUpdateDTO dto) {
		Round round = roundRepository.findByIdAndEventId(id, eventId)
                .orElseThrow(() -> new RoundNotFoundException(
                    id, eventId));

        round.setStatus(dto.getStatus());
        return toDTO(roundRepository.save(round));
	}

	private RoundDTO toDTO(Round round) {
        return RoundDTO.builder()
                .id(round.getId())
                .roundNumber(round.getRoundNumber())
                .pairingType(round.getPairingType())
                .status(round.getStatus())
                .build();
    }
}
