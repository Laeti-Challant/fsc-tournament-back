package com.filsanguinaire.tournament.dto.round;

import com.filsanguinaire.tournament.bo.PairingType;
import com.filsanguinaire.tournament.bo.RoundStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundDTO {

	private Long id;
	
    private int roundNumber;
    
    private PairingType pairingType;
    
    private RoundStatus status;
}
