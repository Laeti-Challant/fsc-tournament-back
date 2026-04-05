package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {

}
