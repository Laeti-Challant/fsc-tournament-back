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
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = {"user_id", "event_id"},
		name = "uk_coach_user_event"))
public class Coach {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private Long id;
	
	@Column(nullable = false, length = 30)
	private String coachPseudo;
	
	@Column(length = 50)
	private String teamName;
	
	@Column(nullable= false, length = 50)
	private String race;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean eating = false;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean vegetarian = false;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CoachStatus status = CoachStatus.PENDING;
	
	@Column(length = 500)
	private String rosterLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RosterStatus rosterStatus = RosterStatus.NOT_SUBMITTED;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean substitute = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
