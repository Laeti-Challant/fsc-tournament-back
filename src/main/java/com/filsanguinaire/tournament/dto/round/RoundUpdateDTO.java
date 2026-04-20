package com.filsanguinaire.tournament.dto.round;

import com.filsanguinaire.tournament.bo.RoundStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundUpdateDTO {

	@NotNull(message = "Le statut est obligatoire")
    private RoundStatus status;
}
