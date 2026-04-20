package com.filsanguinaire.tournament.mapper;

import org.springframework.stereotype.Service;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;

@Service

public class CoachMapper {

	public CoachSummaryDTO toSummaryDTO(Coach coach) {
        return CoachSummaryDTO.builder()
                .id(coach.getId())
                .coachPseudo(coach.getCoachPseudo())
                .teamName(coach.getTeamName())
                .race(coach.getRace())
                .status(coach.getStatus())
                .rosterStatus(coach.getRosterStatus())
                .build();
    }
}
