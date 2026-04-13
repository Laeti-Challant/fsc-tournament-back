package com.filsanguinaire.tournament.bll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.User;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.UserRepository;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;
import com.filsanguinaire.tournament.exceptions.AlreadyCoachRegisteredException;
import com.filsanguinaire.tournament.exceptions.CoachNotFoundException;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements ICoachService {

	private final CoachRepository coachRepository;
	
    private final EventRepository eventRepository;
    
    private final UserRepository userRepository;
    
	@Override
	public Page<CoachSummaryDTO> getAllByEvent(Long eventId, Pageable pageable) {
		return coachRepository.findByEventId(eventId, pageable).map(this::toSummaryDTO);
	}

	@Override
	public CoachDetailDTO getById(Long eventId, Long coachId) {
		Coach coach = coachRepository.findById(coachId).filter(c -> c.getEvent().getId().equals(eventId))
				.orElseThrow(() -> new CoachNotFoundException(coachId));
		return toDetailDTO(coach);
	}

	@Override
	public Page<CoachDetailDTO> getMealsByEvent(Long eventId, Pageable pageable) {
		return coachRepository.findByEventId(eventId, pageable).map(this::toDetailDTO);
	}

	@Override
	public CoachDetailDTO register(Long eventId, Long userId, CoachCreateDTO dto) {
		if (coachRepository.existsByUserIdAndEventId(userId, eventId)) {
			throw new AlreadyCoachRegisteredException();
		}
		
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
		User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
		
		Coach coach = Coach.builder()
				.coachPseudo(dto.getCoachPseudo())
				.teamName(dto.getTeamName())
				.race(dto.getRace())
				.eating(dto.isEating())
				.vegetarian(dto.isVegetarian())
				.event(event)
				.user(user)
				.build();
		return toDetailDTO(coachRepository.save(coach));
	}

	@Override
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
                .userId(coach.getUser().getId())
                .userPseudo(coach.getUser().getPseudo())
                .userEmail(coach.getUser().getEmail())
                .build();
    }

}
