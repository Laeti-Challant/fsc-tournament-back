package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.TournamentRules;

public interface TournamentRulesRepository extends JpaRepository<TournamentRules, Long> {

}
