package com.filsanguinaire.tournament.bll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesCreateUpdateDTO;
import com.filsanguinaire.tournament.exceptions.AlreadyCoachRegisteredException;
import com.filsanguinaire.tournament.exceptions.CoachNotFoundException;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.InvalidRaceException;
import com.filsanguinaire.tournament.exceptions.RegistrationClosedException;
import com.filsanguinaire.tournament.exceptions.TournamentFullException;
import com.filsanguinaire.tournament.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements ICoachService {

	private final CoachRepository coachRepository;
	
    private final EventRepository eventRepository;
    
    private final UserRepository userRepository;
    
    private final TournamentRepository tournamentRepository;
    
    private final TournamentRulesRepository tournamentRulesRepository;
    
	@Override
	public Page<CoachSummaryDTO> getAllByEvent(Long eventId, Pageable pageable) {
		if (!eventRepository.existsById(eventId) ) {
			throw new EventNotFoundException(eventId);
		}
		return coachRepository.findByEventId(eventId, pageable).map(this::toSummaryDTO);
	}

	@Override
	@Transactional
	public CoachDetailDTO getMyCoach(Long eventId, Long userId) {
		return coachRepository.findByUserIdAndEventId(userId, eventId)
	            .map(this::toDetailDTO)
	            .orElse(null);
	}
	
	@Override
	@Transactional
	public CoachDetailDTO getById(Long eventId, Long coachId) {
		Coach coach = coachRepository.findById(coachId).filter(c -> c.getEvent().getId().equals(eventId))
				.orElseThrow(() -> new CoachNotFoundException(coachId));
		return toDetailDTO(coach);
	}

	@Override
	@Transactional
	public Page<CoachDetailDTO> getMealsByEvent(Long eventId, Pageable pageable) {
		return coachRepository.findByEventId(eventId, pageable).map(this::toDetailDTO);
	}

	@Override
	@Transactional
	public CoachDetailDTO register(Long eventId, Long userId, CoachCreateDTO dto) {
		if (coachRepository.existsByUserIdAndEventId(userId, eventId)) {
			throw new AlreadyCoachRegisteredException();
		}
		
		Tournament tournament = tournamentRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
		
		if (tournament.getStatus() != EventStatus.PLANNED) {
			throw new RegistrationClosedException();
		}
		
		if (LocalDate.now().isAfter(tournament.getRegistrationDeadline())) {
			throw new RegistrationClosedException();
		}
		
		long currentCount = coachRepository.countByEventIdAndStatusIn(eventId, List.of(CoachStatus.PENDING, CoachStatus.VALIDATED));
		if (currentCount >= tournament.getMaxParticipants()) {
			throw new TournamentFullException(tournament.getMaxParticipants());
		}
		
		TournamentRules rules =tournamentRulesRepository.findByTournamentId(eventId).orElseThrow(() -> new IllegalStateException("Aucun ruleset configuré pour le tournoi  " + eventId));

		boolean raceExists = rules.getRosterCategories().stream().anyMatch(rc -> rc.getRaceName().equalsIgnoreCase(dto.getRace()));
		if (!raceExists) {
			throw new InvalidRaceException(dto.getRace());
		}
		
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		
		Coach coach = Coach.builder()
				.coachPseudo(dto.getCoachPseudo())
				.teamName(dto.getTeamName())
				.race(dto.getRace())
				.eating(dto.isEating())
				.vegetarian(dto.isVegetarian())
				.event(tournament)
				.user(user)
				.build();
		
		return toDetailDTO(coachRepository.save(coach));
	}

	@Override
	@Transactional
	public CoachDetailDTO adminUpdate(Long eventId, Long coachId, CoachUpdateDTO dto) {
		Coach coach = coachRepository.findById(coachId)
	            .filter(c -> c.getEvent().getId().equals(eventId))
	            .orElseThrow(() -> new CoachNotFoundException(coachId));

	    coach.setCoachPseudo(dto.getCoachPseudo());
	    coach.setTeamName(dto.getTeamName());
	    coach.setRace(dto.getRace());
	    coach.setEating(dto.isEating());
	    coach.setVegetarian(dto.isVegetarian());

	    return toDetailDTO(coachRepository.save(coach));
	}

	@Override
	public void delete(Long eventId, Long coachId) {
		Coach coach = coachRepository.findById(coachId)
	            .filter(c -> c.getEvent().getId().equals(eventId))
	            .orElseThrow(() -> new CoachNotFoundException(coachId));

	    coachRepository.delete(coach);

	}
	
	private CoachSummaryDTO toSummaryDTO(Coach coach) {
        return CoachSummaryDTO.builder()
                .id(coach.getId())
                .coachPseudo(coach.getCoachPseudo())
                .teamName(coach.getTeamName())
                .race(coach.getRace())
                .status(coach.getStatus())
                .rosterStatus(coach.getRosterStatus())
                .build();
    }

    private CoachDetailDTO toDetailDTO(Coach coach) {
        return CoachDetailDTO.builder()
                .id(coach.getId())
                .coachPseudo(coach.getCoachPseudo())
                .teamName(coach.getTeamName())
                .race(coach.getRace())
                .eating(coach.isEating())
                .vegetarian(coach.isVegetarian())
                .status(coach.getStatus())
                .rosterStatus(coach.getRosterStatus())
                .rosterLink(coach.getRosterLink())
                .substitute(coach.isSubstitute())
                .userId(coach.getUser().getId())
                .userPseudo(coach.getUser().getPseudo())
                .userEmail(coach.getUser().getEmail())
                .build();
    }
}
