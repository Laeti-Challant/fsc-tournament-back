package com.filsanguinaire.tournament.dto.tournament;

import com.filsanguinaire.tournament.dto.event.EventCreateUpdateDTO;

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

	@Size(max = 100)
	private String location;

	@Size(max = 150)
	private String address;

	@Pattern(regexp = "^[0-9]{5}$", message = "Code postal invalide (5 chiffres)")
	private String postalCode;

	@Size(max = 50)
	private String city;
}
