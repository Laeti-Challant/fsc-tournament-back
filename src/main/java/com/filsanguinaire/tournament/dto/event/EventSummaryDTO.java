package com.filsanguinaire.tournament.dto.event;

import java.time.LocalDate;

import com.filsanguinaire.tournament.bo.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDTO {

	private Long id;
	
    private String name;
    
    private LocalDate eventDate;
    
    private EventStatus status;
    
    private String type;
}
