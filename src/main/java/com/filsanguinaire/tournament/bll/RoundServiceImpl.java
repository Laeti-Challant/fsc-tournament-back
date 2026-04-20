package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.dto.round.RoundCreateDTO;
import com.filsanguinaire.tournament.dto.round.RoundDTO;
import com.filsanguinaire.tournament.dto.round.RoundUpdateDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.RoundNotFoundException;

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
	public RoundDTO create(Long eventId, RoundCreateDTO dto) {
		Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Un numéro de round doit être unique par event
        if (roundRepository.existsByEventIdAndRoundNumber(eventId, dto.getRoundNumber())) {
            throw new IllegalArgumentException(
                "Un round numéro " + dto.getRoundNumber() + " existe déjà pour cet event");
        }
        
        Round round = Round.builder()
                .roundNumber(dto.getRoundNumber())
                .pairingType(dto.getPairingType())
                .event(event)
                .build();

        return toDTO(roundRepository.save(round));
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
