package com.filsanguinaire.tournament.bo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class TournamentRules {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	private Long id;
	
	@Column(nullable = false)
	private int budgetPo;
	
	@Column(nullable = false)
	private short pspPool;
	
	@Column(nullable = false)
	private short maxSkillsPerPlayer;
	
	@Column(nullable = false)
	private boolean resurrectionMode = true;
	
	@Column(nullable = false)
	private short mogettePspValue;
	
	@Column(nullable = false)
	private int mogettePoValue;
	
	@Column(columnDefinition = "TEXT")
	private String notesText;
	
	@Column(columnDefinition = "TEXT")
	private String rosterText;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", unique = true)
	private Event event;
	
	@Builder.Default
	@OneToMany(mappedBy = "rules", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AllowedInducement> allowedInducements = new ArrayList<>();
	
	@Builder.Default
	@OneToMany(mappedBy = "rules", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RosterCategory> rosterCategories = new ArrayList<>();
}
