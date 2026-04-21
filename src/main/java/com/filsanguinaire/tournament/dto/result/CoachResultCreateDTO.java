package com.filsanguinaire.tournament.dto.result;

import com.filsanguinaire.tournament.bo.MatchResult;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoachResultCreateDTO {

	@NotNull
    private MatchResult result;
	
    @Min(0)
    private int touchdowns;
    
    @Min(0)
    private int casualties;
    
    @Min(0)
    private int objectives;
    
    private boolean bonusObjective;
    
    @Min(0)
    private int passes;
    
    @Min(0)
    private int foulActions;
    
    @NotNull
    private Long coachId;
}
