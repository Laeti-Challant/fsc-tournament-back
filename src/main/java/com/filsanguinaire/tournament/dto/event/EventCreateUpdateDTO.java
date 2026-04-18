package com.filsanguinaire.tournament.dto.event;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateUpdateDTO {

	@NotBlank(message = "Le nom de l'event est obligatoire")
    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String name;
    
	@NotNull(message = "La date de l'event est obligatoire")
    @Future(message = "La date de l'event doit être dans le futur")
    private LocalDate eventDate;
    
	@NotNull(message = "La date limite d'inscription est obligatoire")
    @Future(message = "La date limite d'inscription doit être dans le futur")
    private LocalDate registrationDeadline;
    
	@NotNull(message = "Le nombre de participants est obligatoire")
    @Min(value = 2, message = "Il faut au moins 2 participants")
    private int maxParticipants;
    
	@NotNull(message = "Le nombre de rounds est obligatoire")
    @Min(value = 1, message = "Il faut au moins 1 round")
    private int nbRounds;
}