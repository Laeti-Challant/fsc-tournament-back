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
public class TournamentRulesCreateUpdateDTO {

    private Integer budgetPo;
    
    private Integer pspPool;
    
    private Integer maxSkillsPerPlayer;
    
    private Boolean resurrectionMode;
    
    private Integer mogettePspValue;
    
    private Integer mogettePoValue;
    
    private String notesText;
    
    private String rosterText;
    
    private List<AllowedInducementDTO> allowedInducements;
    
    private List<RosterCategoryDTO> rosterCategories;
}