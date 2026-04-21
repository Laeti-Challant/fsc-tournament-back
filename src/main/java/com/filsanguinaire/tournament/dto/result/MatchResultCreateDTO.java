package com.filsanguinaire.tournament.dto.result;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultCreateDTO {

	@Valid @NotNull
    private CoachResultCreateDTO coach1Result;
	
    @Valid @NotNull
    private CoachResultCreateDTO coach2Result;
}
