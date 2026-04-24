package com.filsanguinaire.tournament.dto.event;

import java.time.LocalDate;

import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDTO {

	private Long id;
	
    private String name;
    
    private LocalDate eventDate;
    
    private LocalDate registrationDeadline;
    
    private EventStatus status;
    
    private int maxParticipants;
    
    private int nbRounds;
    
    private String type;
    
    private String location;
    
    private String address;
    
    private String postalCode;
    
    private String city;
    
    private boolean featured;
    
    private TournamentRulesDTO rules;
}
