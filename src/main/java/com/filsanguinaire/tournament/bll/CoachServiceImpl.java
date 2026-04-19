package com.filsanguinaire.tournament.bll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.coach.CoachAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachMealDTO;
import com.filsanguinaire.tournament.dto.coach.CoachParticipantDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;
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
	public Page<CoachSummaryDTO> getAllByTournament(Long tournamentId, Pageable pageable) {
		if (!eventRepository.existsById(tournamentId) ) {
			throw new EventNotFoundException(tournamentId);
		}
		return coachRepository.findByEventId(tournamentId, pageable).map(this::toSummaryDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CoachDetailDTO> getMyCoach(Long tournamentId, Long userId) {
		return coachRepository.findByUserIdAndEventId(userId, tournamentId)
	            .map(this::toDetailDTO);
	}
	
	@Override
	@Transactional
	public CoachDetailDTO getById(Long tournamentId, Long coachId) {
		Coach coach = coachRepository.findById(coachId).filter(c -> c.getEvent().getId().equals(tournamentId))
				.orElseThrow(() -> new CoachNotFoundException(coachId));
		return toDetailDTO(coach);
	}

	@Override
	@Transactional
	public List<CoachMealDTO> getMealsByTournament(Long tournamentId) {
		if (!eventRepository.existsById(tournamentId)) {
			throw new EventNotFoundException(tournamentId);
		}
		return coachRepository.findByEventIdAndStatusAndEatingTrue(tournamentId, CoachStatus.VALIDATED)
	            .stream()
	            .map(this::toMealDTO)
	            .toList();
	}

	@Override
	@Transactional
	public CoachDetailDTO register(Long tournamentId, Long userId, CoachCreateDTO dto) {
		if (coachRepository.existsByUserIdAndEventId(userId, tournamentId)) {
			throw new AlreadyCoachRegisteredException();
		}
		
		Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EventNotFoundException(tournamentId));
		
		if (tournament.getStatus() != EventStatus.PLANNED) {
			throw new RegistrationClosedException();
		}
		
		if (LocalDate.now().isAfter(tournament.getRegistrationDeadline())) {
			throw new RegistrationClosedException();
		}
		
		long currentCount = coachRepository.countByEventIdAndStatusIn(tournamentId, List.of(CoachStatus.PENDING, CoachStatus.VALIDATED));
		if (currentCount >= tournament.getMaxParticipants()) {
			throw new TournamentFullException(tournament.getMaxParticipants());
		}
		
		TournamentRules rules =tournamentRulesRepository.findByTournamentId(tournamentId).orElseThrow(() -> new IllegalStateException("Aucun ruleset configuré pour le tournoi  " + tournamentId));

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
	public CoachDetailDTO adminUpdate(Long tournamentId, Long coachId, CoachAdminUpdateDTO dto) {
		Coach coach = coachRepository.findById(coachId)
	            .filter(c -> c.getEvent().getId().equals(tournamentId))
	            .orElseThrow(() -> new CoachNotFoundException(coachId));

		coach.setStatus(dto.getStatus());
	    coach.setRosterStatus(dto.getRosterStatus());
	    coach.setSubstitute(dto.isSubstitute());

	    return toDetailDTO(coachRepository.save(coach));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CoachParticipantDTO> getValidatedParticipants(Long tournamentId) {
	    if (!eventRepository.existsById(tournamentId)) {
	        throw new EventNotFoundException(tournamentId);
	    }
	    return coachRepository.findByEventIdAndStatusAndSubstituteFalse(tournamentId, CoachStatus.VALIDATED)
	            .stream()
	            .map(this::toParticipantDTO)
	            .toList();
	}

	@Override
	public void delete(Long tournamentId, Long coachId) {
		Coach coach = coachRepository.findById(coachId)
	            .filter(c -> c.getEvent().getId().equals(tournamentId))
	            .orElseThrow(() -> new CoachNotFoundException(coachId));

	    coachRepository.delete(coach);

	}
	
	@Override
	@Transactional
	public CoachDetailDTO updateMe(long eventId, long userId, CoachUpdateDTO dto) {
		Coach coach = coachRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() -> new CoachNotFoundException(userId));
		
		Tournament tournament = tournamentRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
		
		if (tournament.getStatus() != EventStatus.PLANNED) {
			throw new RegistrationClosedException();
		}
		
		if (!coach.getRace().equalsIgnoreCase(dto.getRace())) {
			TournamentRules rules = tournamentRulesRepository.findByTournamentId(eventId).orElseThrow(() ->  new IllegalStateException("Aucun Ruleset configuré pour le tournoi" + eventId));
			
			boolean raceExists = rules.getRosterCategories().stream().anyMatch(rc -> rc.getRaceName().equalsIgnoreCase(dto.getRace()));
			if (!raceExists) {
				throw new InvalidRaceException(dto.getRace());
			}
		}
		
		coach.setCoachPseudo(dto.getCoachPseudo());
	    coach.setTeamName(dto.getTeamName());
	    coach.setRace(dto.getRace());
	    coach.setEating(dto.isEating());
	    coach.setVegetarian(dto.isVegetarian());
	    coach.setRosterLink(dto.getRosterLink());

	    return toDetailDTO(coachRepository.save(coach));
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
    
    private CoachMealDTO toMealDTO(Coach coach) {
        return CoachMealDTO.builder()
                .id(coach.getId())
                .coachPseudo(coach.getCoachPseudo())
                .teamName(coach.getTeamName())
                .race(coach.getRace())
                .vegetarian(coach.isVegetarian())
                .build();
    }

    private CoachParticipantDTO toParticipantDTO(Coach coach) {
        return CoachParticipantDTO.builder()
                .id(coach.getId())
                .coachPseudo(coach.getCoachPseudo())
                .teamName(coach.getTeamName())
                .race(coach.getRace())
                .build();
    }
}
