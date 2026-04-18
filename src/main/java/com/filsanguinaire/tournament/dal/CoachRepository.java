package com.filsanguinaire.tournament.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;

public interface CoachRepository extends JpaRepository<Coach, Long> {

	// Liste paginée des coaches d'un event
    Page<Coach> findByEventId(Long eventId, Pageable pageable);

    // Vérifier si un user est déjà inscrit à un event
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    // Récupérer le coach d'un user pour un event donné 
    Optional<Coach> findByUserIdAndEventId(Long userId, Long eventId);
    
 // Compter les coaches PENDING + VALIDATED pour vérifier maxParticipants
    long countByEventIdAndStatusIn(Long eventId, List<CoachStatus> statuses);

    // Liste des coaches validés (non paginée)
    List<Coach> findByEventIdAndStatus(Long eventId, CoachStatus status);
}
