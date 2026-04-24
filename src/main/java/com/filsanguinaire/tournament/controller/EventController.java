package com.filsanguinaire.tournament.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.IEventService;
import com.filsanguinaire.tournament.dto.event.EventDetailDTO;
import com.filsanguinaire.tournament.dto.event.EventSummaryDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

	private final IEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventSummaryDTO>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }    
}
