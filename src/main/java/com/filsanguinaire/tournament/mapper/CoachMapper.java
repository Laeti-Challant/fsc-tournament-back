package com.filsanguinaire.tournament.mapper;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.dto.coach.CoachSummaryDTO;

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
