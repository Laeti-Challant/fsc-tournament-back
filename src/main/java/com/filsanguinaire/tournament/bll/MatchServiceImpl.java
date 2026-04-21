package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Match;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.MatchRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.dto.match.MatchCreateDTO;
import com.filsanguinaire.tournament.dto.match.MatchDTO;
import com.filsanguinaire.tournament.exceptions.ChallengeAlreadyExistsException;
import com.filsanguinaire.tournament.exceptions.CoachNotFoundException;
import com.filsanguinaire.tournament.exceptions.CoachNotValidatedException;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.MatchNotFoundException;
import com.filsanguinaire.tournament.exceptions.RoundNotFoundException;
import com.filsanguinaire.tournament.exceptions.TournamentNotEditableException;
import com.filsanguinaire.tournament.mapper.CoachMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements IMatchService {

	private final MatchRepository matchRepository;
	private final RoundRepository roundRepository;
	private final CoachRepository coachRepository;
	private final EventRepository eventRepository;
	private final CoachMapper coachMapper;

	@Override
	public List<MatchDTO> findAllByRound(Long eventId, Long roundId) {
		if (!roundRepository.findByIdAndEventId(roundId, eventId).isPresent()) {
			throw new RoundNotFoundException(roundId, eventId);
		}
		return matchRepository.findByRoundId(roundId).stream().map(this::toDTO).toList();
	}

	@Override
	public MatchDTO findByIdAndRound(Long id, Long roundId, Long eventId) {
		roundRepository.findByIdAndEventId(roundId, eventId)
				.orElseThrow(() -> new RoundNotFoundException(roundId, eventId));

		Match match = matchRepository.findByIdAndRoundId(id, roundId)
				.orElseThrow(() -> new MatchNotFoundException(id, roundId));

		return toDTO(match);
	}

	@Override
	public MatchDTO createChallenge(Long eventId, Long roundId, MatchCreateDTO dto) {
		Round round = roundRepository.findByIdAndEventId(roundId, eventId)
				.orElseThrow(() -> new RoundNotFoundException(roundId, eventId));
		
		Event event = eventRepository.findById(eventId)
		        .orElseThrow(() -> new EventNotFoundException(eventId));

		if (event.getStatus() == EventStatus.IN_PROGRESS
		        || event.getStatus() == EventStatus.FINISHED) {
		    throw new TournamentNotEditableException();
		}

		Coach coach1 = getValidatedCoach(dto.getCoach1Id(), eventId);
		Coach coach2 = getValidatedCoach(dto.getCoach2Id(), eventId);

		// Un coach ne peut avoir qu'un seul match par round
		if (matchRepository.existsCoachInRound(roundId, coach1.getId())) {
			throw new ChallengeAlreadyExistsException(coach1.getCoachPseudo());
		}
		if (matchRepository.existsCoachInRound(roundId, coach2.getId())) {
			throw new ChallengeAlreadyExistsException(coach2.getCoachPseudo());
		}

		Match match = Match.builder().round(round).coach1(coach1).coach2(coach2).build();

		return toDTO(matchRepository.save(match));
	}

	private Coach getValidatedCoach(Long coachId, Long eventId) {
		Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new CoachNotFoundException(coachId));
		if (coach.getStatus() != CoachStatus.VALIDATED) {
			throw new CoachNotValidatedException(coach.getCoachPseudo());
		}
		if (!coach.getEvent().getId().equals(eventId)) {
			throw new CoachNotValidatedException(coach.getCoachPseudo());
		}
		return coach;
	}

	private MatchDTO toDTO(Match match) {
		return MatchDTO.builder().id(match.getId()).status(match.getStatus())
				.coach1(coachMapper.toSummaryDTO(match.getCoach1())).coach2(coachMapper.toSummaryDTO(match.getCoach2()))
				.build();
	}
}
