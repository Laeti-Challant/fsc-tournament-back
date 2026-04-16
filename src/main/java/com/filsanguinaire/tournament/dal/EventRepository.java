package com.filsanguinaire.tournament.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findAllByStatus(EventStatus status);
}
