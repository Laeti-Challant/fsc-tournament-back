package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
