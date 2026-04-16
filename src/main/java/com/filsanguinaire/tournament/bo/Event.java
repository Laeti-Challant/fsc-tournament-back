package com.filsanguinaire.tournament.bo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Event {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private Long id;
	
	@Column(nullable =  false, length = 50)
	private String name;
	
	@Column(nullable =  false)
	private LocalDate eventDate;
	
	@Column(nullable =  false)
	private LocalDate registrationDeadline;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EventStatus status;
	
	@Column(nullable =  false)
	private int maxParticipants;
	
	@Column(nullable =  false)
	private int nbRounds;
}
