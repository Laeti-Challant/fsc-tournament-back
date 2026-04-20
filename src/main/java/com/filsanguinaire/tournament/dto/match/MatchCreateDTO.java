package com.filsanguinaire.tournament.dto.match;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchCreateDTO {

	@NotNull(message = "Le coach 1 est obligatoire")
    private Long coach1Id;

    @NotNull(message = "Le coach 2 est obligatoire")
    private Long coach2Id;
}
