package com.filsanguinaire.tournament.dto.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RosterCategoryDTO {

    private Long id;
    
    private String raceName;
    
    private short categoryValue;
    
    private boolean isMinus;
}