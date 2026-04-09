package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	// Trouver l'event IN_PROGRESS
	Optional<Event> findFirstByStatusIn(List<EventStatus> statuses);
	
	List<Event> findAllByStatus(EventStatus status);
}
