package com.filsanguinaire.tournament.dto.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRulesDTO {

    private Long id;
    
    private int budgetPo;
    
    private short pspPool;
    
    private short maxSkillsPerPlayer;
    
    private boolean resurrectionMode;
    
    private short mogettePspValue;
    
    private int mogettePoValue;
    
    private String notesText;
    
    private String rosterText;
    
    private List<AllowedInducementDTO> allowedInducements;
    
    private List<RosterCategoryDTO> rosterCategories;
}