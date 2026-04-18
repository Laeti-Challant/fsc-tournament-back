package com.filsanguinaire.tournament.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "match")
public class Match {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false,  length = 10)
	@Builder.Default
	private MatchStatus status = MatchStatus.PENDING;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "round_id", nullable= false)
	private Round round;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach1_id", nullable = false)
	private Coach coach1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach2_id", nullable = false)
	private Coach coach2;
}
