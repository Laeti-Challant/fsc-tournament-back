package com.filsanguinaire.tournament.dto.rules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"minus"})
public class RosterCategoryDTO {

    private Long id;
    
    @NotBlank(message = "Le nom de la race est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String raceName;

    @NotNull(message = "La valeur de catégorie est obligatoire")
    private Integer categoryValue;
    
    @JsonProperty("isMinus")
    private boolean isMinus;
}