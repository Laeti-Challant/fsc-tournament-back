package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
