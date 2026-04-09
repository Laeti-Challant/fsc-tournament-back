package com.filsanguinaire.tournament.dto.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllowedInducementDTO {

    private Long id;
    
    private String name;
    
    private Integer minQty;
    
    private Integer maxQty;
}
