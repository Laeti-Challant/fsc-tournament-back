package com.filsanguinaire.tournament.dto.rules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    private String raceName;
    
    private short categoryValue;
    
    @JsonProperty("isMinus")
    private boolean isMinus;
}