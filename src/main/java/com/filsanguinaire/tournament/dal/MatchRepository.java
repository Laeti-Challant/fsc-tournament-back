package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.filsanguinaire.tournament.bo.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

	// Liste tous les matches d'un round
	@EntityGraph(attributePaths = {"coach1", "coach2"})
	List<Match> findByRoundId(Long roundId);

    // Récupère un match en s'assurant qu'il appartient au bon round
	@EntityGraph(attributePaths = {"coach1", "coach2"})
	Optional<Match> findByIdAndRoundId(Long id, Long roundId);

    // Vérifie qu'un coach est déjà dans un match du round (coach1 OU coach2)
    // Utilisé pour la validation des défis : un coach ne peut avoir qu'un seul défi par round
    @Query("""
        SELECT COUNT(m) > 0 FROM Match m
        WHERE m.round.id = :roundId
        AND (m.coach1.id = :coachId OR m.coach2.id = :coachId)
        """)
    boolean existsCoachInRound(@Param("roundId") Long roundId, @Param("coachId") Long coachId);

    // Vérifie que deux coaches n'ont pas déjà joué ensemble sur cet event
    // Utilisé par le PairingService pour l'appariement Swiss (éviter les doublons)
    @Query("""
        SELECT COUNT(m) > 0 FROM Match m
        WHERE m.round.event.id = :eventId
        AND (
            (m.coach1.id = :coach1Id AND m.coach2.id = :coach2Id)
            OR
            (m.coach1.id = :coach2Id AND m.coach2.id = :coach1Id)
        )
        """)
    boolean haveAlreadyPlayed(
        @Param("eventId") Long eventId,
        @Param("coach1Id") Long coach1Id,
        @Param("coach2Id") Long coach2Id
    );
}
