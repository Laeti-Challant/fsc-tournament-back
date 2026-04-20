package com.filsanguinaire.tournament.bll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.Match;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.MatchRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.dto.match.MatchDTO;
import com.filsanguinaire.tournament.exceptions.PairingException;
import com.filsanguinaire.tournament.exceptions.RoundNotFoundException;
import com.filsanguinaire.tournament.mapper.CoachMapper;

public class PairingServiceImpl implements IPairingsService {

	private final RoundRepository roundRepository;
    private final MatchRepository matchRepository;
    private final CoachRepository coachRepository;
    private final CoachMapper coachMapper;
    
	@Override
	public List<MatchDTO> generatepairings(Long eventId, Long roundId) {
		// 1. Vérifie que le round appartient à l'event
        Round round = roundRepository.findByIdAndEventId(roundId, eventId)
                .orElseThrow(() -> new RoundNotFoundException(roundId, eventId));

        // 2. Tous les coaches validés de l'event (substitutes inclus)
        List<Coach> allCoaches = coachRepository
                .findByEventIdAndStatus(eventId, CoachStatus.VALIDATED);

        // 3. Coaches déjà appariés dans ce round (les défis pré-créés)
        List<Match> existingMatches = matchRepository.findByRoundId(roundId);
        Set<Long> alreadyPairedIds = existingMatches.stream()
                .flatMap(m -> Stream.of(m.getCoach1().getId(), m.getCoach2().getId()))
                .collect(Collectors.toSet());

        // 4. Coaches restants à apparier
        List<Coach> remaining = allCoaches.stream()
                .filter(c -> !alreadyPairedIds.contains(c.getId()))
                .collect(Collectors.toList());

        // 5. Nombre impair = impossible d'apparier
        if (remaining.size() % 2 != 0) {
            throw new PairingException(
                "Nombre impair de coaches à apparier (" + remaining.size() + "). "
                + "Veuillez ajouter un remplaçant avant de générer les appariements.");
        }

        // 6. Génération selon le type d'appariement
        List<Match> newMatches = switch (round.getPairingType()) {
            case RANDOM -> generateRandom(remaining, round);
            // TODO : mettre en place l'appariement suisse (feature 9)
            case SWISS  -> throw new PairingException(
                "L'appariement Swiss sera disponible après implémentation du classement général.");
        };

        // 7. Sauvegarde des nouveaux matches
        matchRepository.saveAll(newMatches);

        // 8. Retourne tous les matches du round (défis + nouveaux)
        return matchRepository.findByRoundId(roundId)
                .stream()
                .map(this::toDTO)
                .toList();
	}

	private List<Match> generateRandom(List<Coach> coaches, Round round) {
        List<Coach> shuffled = new ArrayList<>(coaches);
        Collections.shuffle(shuffled);

        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < shuffled.size(); i += 2) {
            matches.add(Match.builder()
                    .round(round)
                    .coach1(shuffled.get(i))
                    .coach2(shuffled.get(i + 1))
                    .build());
        }
        return matches;
    }

    private MatchDTO toDTO(Match match) {
        return MatchDTO.builder()
                .id(match.getId())
                .status(match.getStatus())
                .coach1(coachMapper.toSummaryDTO(match.getCoach1()))
                .coach2(coachMapper.toSummaryDTO(match.getCoach2()))
                .build();
    }
}
