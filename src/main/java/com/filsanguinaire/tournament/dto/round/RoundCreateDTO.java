package com.filsanguinaire.tournament.dto.round;

import com.filsanguinaire.tournament.bo.PairingType;

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
public class RoundCreateDTO {

	@Min(value = 1, message = "Le numéro de round doit être supérieur à 0")
    private int roundNumber;

    @NotNull(message = "Le type d'appariement est obligatoire")
    private PairingType pairingType;
}
