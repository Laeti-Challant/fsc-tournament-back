package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

	Optional<Tournament> findFirstByStatusIn(List<EventStatus> statuses);
	
	List<Tournament> findAllByStatusIn(List<EventStatus> statuses);
	
    Optional<Tournament> findByFeaturedTrue();
}
