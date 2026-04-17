package com.filsanguinaire.tournament.bll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.filsanguinaire.tournament.dto.coach.CoachAdminUpdateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachCreateDTO;
import com.filsanguinaire.tournament.dto.coach.CoachDetailDTO;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;
import com.filsanguinaire.tournament.dto.coach.CoachUpdateDTO;

public interface ICoachService {

	Page<CoachSummaryDTO> getAllByEvent(Long eventId, Pageable pageable);
	
    CoachDetailDTO getById(Long eventId, Long coachId);
    
    Page<CoachDetailDTO> getMealsByEvent(Long eventId, Pageable pageable);
    
    CoachDetailDTO getMyCoach(Long eventId, Long userId);
    
    CoachDetailDTO register(Long eventId, Long userId, CoachCreateDTO dto); 
    
    CoachDetailDTO adminUpdate(Long eventId, Long coachId, CoachAdminUpdateDTO dto);
    
    void delete(Long eventId, Long coachId);

	CoachDetailDTO updateMe(long eventId, long userId, CoachUpdateDTO dto);
}
