package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Coach;

public interface CoachRepository extends JpaRepository<Coach, Long> {

}
