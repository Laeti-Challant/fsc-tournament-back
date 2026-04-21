package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {

	// Liste des rounds d'un event, triée par numéro de round
    List<Round> findByEventIdOrderByRoundNumberAsc(Long eventId);

    // Récupère un round en s'assurant qu'il appartient à l'event
    Optional<Round> findByIdAndEventId(Long id, Long eventId);

    // Vérifie qu'un numéro de round n'est pas déjà utilisé sur cet event
    boolean existsByEventIdAndRoundNumber(Long eventId, int roundNumber);
}
