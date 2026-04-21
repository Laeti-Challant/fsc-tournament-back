package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.CoachResult;

public interface CoachResultRepository extends JpaRepository<CoachResult, Long> {

	// GET des résultats d'un match
    @EntityGraph(attributePaths = {"coach", "coach.user"})
    List<CoachResult> findAllByMatch_Id(Long matchId);

    // Vérifier si les résultats existent déjà — pour l'exception ResultAlreadyExistsException
    boolean existsByMatch_Id(Long matchId);

    // Vérifier si un coach a déjà un résultat sur ce match — sécurité supplémentaire
    boolean existsByMatch_IdAndCoach_Id(Long matchId, Long coachId);

    // Pour le PUT — retrouver un résultat par son id ET le matchId (sécurité URL)
    Optional<CoachResult> findByIdAndMatch_Id(Long id, Long matchId);

    // Pour Feature 9 — classements par event (à ne pas oublier)
    @EntityGraph(attributePaths = {"coach", "coach.user", "match", "match.round"})
    List<CoachResult> findAllByMatch_Round_Event_Id(Long eventId);
}
