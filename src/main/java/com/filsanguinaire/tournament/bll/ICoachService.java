package com.filsanguinaire.tournament.bll;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.filsanguinaire.tournament.dto.coach.CoachAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachMealDTO;
import com.filsanguinaire.tournament.dto.coach.CoachParticipantDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;

public interface ICoachService {

	Page<CoachSummaryDTO> getAllByTournament(Long tournamentId, Pageable pageable);
	
    CoachDetailDTO getById(Long tournamenttId, Long coachId);
    
    List<CoachMealDTO> getMealsByTournament(Long tournamentId);
    
    Optional<CoachDetailDTO> getMyCoach(Long tournamentId, Long userId);
    
    CoachDetailDTO register(Long tournamentId, Long userId, CoachCreateDTO dto); 
    
    CoachDetailDTO adminUpdate(Long tournamentId, Long coachId, CoachAdminUpdateDTO dto);
    
    void delete(Long tournamentId, Long coachId);

	CoachDetailDTO updateMe(long tournamentId, long userId, CoachUpdateDTO dto);
	
	List<CoachParticipantDTO> getValidatedParticipants(Long tournamentId);
}
