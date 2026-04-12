package com.filsanguinaire.tournament.dto.rules;

import jakarta.validation.constraints.Min;
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
public class AllowedInducementDTO {

    private Long id;
    
    @NotBlank(message = "Le nom de l'inducement est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String name;

    @NotNull(message = "La quantité minimale est obligatoire")
    @Min(value = 0, message = "La quantité minimale ne peut pas être négative")
    private Short minQty;

    @NotNull(message = "La quantité maximale est obligatoire")
    @Min(value = 1, message = "La quantité maximale doit être au moins 1")
    private Short maxQty;
}
