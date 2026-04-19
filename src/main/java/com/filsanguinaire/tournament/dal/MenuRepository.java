package com.filsanguinaire.tournament.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findByTournamentId(Long tournamentId);
}
