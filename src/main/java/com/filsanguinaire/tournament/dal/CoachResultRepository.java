package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.CoachResult;

public interface CoachResultRepository extends JpaRepository<CoachResult, Long> {

}
