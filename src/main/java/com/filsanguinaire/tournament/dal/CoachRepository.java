package com.filsanguinaire.tournament.dal;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.Coach;

public interface CoachRepository extends JpaRepository<Coach, Long> {

	// Liste paginée des coaches d'un event
    Page<Coach> findByEventId(Long eventId, Pageable pageable);

    // Vérifier si un user est déjà inscrit à un event
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    // Récupérer le coach d'un user pour un event donné (utile pour /inscription)
    Optional<Coach> findByUserIdAndEventId(Long userId, Long eventId);
}
