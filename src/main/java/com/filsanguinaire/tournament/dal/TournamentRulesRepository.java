package com.filsanguinaire.tournament.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.TournamentRules;

public interface TournamentRulesRepository extends JpaRepository<TournamentRules, Long> {

	Optional<TournamentRules> findByTournamentId(Long tournamentId);
	
	boolean existsByTournamentId(Long tournamentId);
}
