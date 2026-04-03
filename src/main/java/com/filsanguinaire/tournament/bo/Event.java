package com.filsanguinaire.tournament.bo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity
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
	private EventStatus status = EventStatus.PLANNED;
	
	@Column(nullable =  false)
	private int maxParticipants;
	
	@Column(nullable =  false)
	private int nbRounds;
}
