package com.filsanguinaire.tournament.dto.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRulesCreateUpdateDTO {

	@NotNull(message = "Le budget en po est obligatoire")
    @Min(value = 1, message = "Le budget doit être positif")
    private int budgetPo;
    
	@NotNull(message = "Le pool de PSP est obligatoire")
    @Min(value = 0, message = "Le pool de PSP ne peut pas être négatif")
    private short pspPool;
    
	@NotNull(message = "Le nombre max de compétences est obligatoire")
    @Min(value = 0, message = "Le nombre max de compétences ne peut pas être négatif")
    private short maxSkillsPerPlayer;
    
	@NotNull(message = "Le mode résurrection est obligatoire")
    private Boolean resurrectionMode;
    
	@NotNull(message = "La valeur mogette en PSP est obligatoire")
    @Min(value = 0, message = "La valeur mogette PSP ne peut pas être négative")
    private short mogettePspValue;
    
	@NotNull(message = "La valeur mogette en po est obligatoire")
    @Min(value = 0, message = "La valeur mogette po ne peut pas être négative")
    private int mogettePoValue;
    
    private String notesText;
    
    private String rosterText;
    
    @Valid
    private List<AllowedInducementDTO> allowedInducements;
    
    @Valid
    private List<RosterCategoryDTO> rosterCategories;
}