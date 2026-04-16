package com.filsanguinaire.tournament.dto.tournament;

import com.filsanguinaire.tournament.dto.event.EventCreateUpdateDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TournamentCreateUpdateDTO extends EventCreateUpdateDTO {

	@NotBlank(message = "Le lieu est obligatoire")
    @Size(max = 100)
    private String location;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 150)
    private String address;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "^[0-9]{5}$", message = "Code postal invalide (5 chiffres)")
    private String postalCode;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 50)
    private String city;
}
