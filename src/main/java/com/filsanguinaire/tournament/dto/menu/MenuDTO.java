package com.filsanguinaire.tournament.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

	private Long id;
	
    private String label;
    
    private String description;
    
    private Integer displayOrder;
}
