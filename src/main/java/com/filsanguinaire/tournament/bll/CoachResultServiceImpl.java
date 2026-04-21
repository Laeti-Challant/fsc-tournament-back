package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachResult;
import com.filsanguinaire.tournament.bo.Match;
import com.filsanguinaire.tournament.bo.MatchResult;
import com.filsanguinaire.tournament.bo.MatchStatus;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.CoachResultRepository;
import com.filsanguinaire.tournament.dal.MatchRepository;
import com.filsanguinaire.tournament.dto.result.CoachResultCreateDTO;
import com.filsanguinaire.tournament.dto.result.CoachResultDTO;
import com.filsanguinaire.tournament.dto.result.MatchResultCreateDTO;
import com.filsanguinaire.tournament.exceptions.CoachNotFoundException;
import com.filsanguinaire.tournament.exceptions.InvalidResultException;
import com.filsanguinaire.tournament.exceptions.MatchNotFoundException;
import com.filsanguinaire.tournament.exceptions.ResultAlreadyExistsException;
import com.filsanguinaire.tournament.mapper.CoachMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CoachResultServiceImpl implements ICoachResultService {

	private final CoachResultRepository coachResultRepository;
	
    private final MatchRepository matchRepository;
    
    private final CoachRepository coachRepository;
    
    private final CoachMapper coachMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<CoachResultDTO> getByMatch(Long eventId, Long roundId, Long matchId) {
        findMatchOrThrow(eventId, roundId, matchId);

        return coachResultRepository.findAllByMatch_Id(matchId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

	@Override
	public List<CoachResultDTO> create(Long eventId, Long roundId, Long matchId, MatchResultCreateDTO dto) {
		Match match = findMatchOrThrow(eventId, roundId, matchId);

		if (!match.getCoach1().getId().equals(dto.getCoach1Result().getCoachId()) ||
			    !match.getCoach2().getId().equals(dto.getCoach2Result().getCoachId())) {
			    throw new InvalidResultException(
			        "Les coaches du résultat ne correspondent pas aux coaches du match."
			    );
			}
		
        if (coachResultRepository.existsByMatch_Id(matchId)) {
            throw new ResultAlreadyExistsException(matchId);
        }

        validateCoherence(dto.getCoach1Result().getResult(),
                          dto.getCoach2Result().getResult());

        Coach coach1 = findCoachOrThrow(dto.getCoach1Result().getCoachId());
        Coach coach2 = findCoachOrThrow(dto.getCoach2Result().getCoachId());

        CoachResult result1 = buildCoachResult(dto.getCoach1Result(), coach1, match);
        CoachResult result2 = buildCoachResult(dto.getCoach2Result(), coach2, match);

        coachResultRepository.save(result1);
        coachResultRepository.save(result2);

        match.setStatus(MatchStatus.FINISHED);
        matchRepository.save(match);

        return List.of(toDTO(result1), toDTO(result2));
	}

	@Override
	public List<CoachResultDTO> update(Long eventId, Long roundId, Long matchId, MatchResultCreateDTO dto) {
		Match match = findMatchOrThrow(eventId, roundId, matchId);
		
		if (!match.getCoach1().getId().equals(dto.getCoach1Result().getCoachId()) ||
			    !match.getCoach2().getId().equals(dto.getCoach2Result().getCoachId())) {
			    throw new InvalidResultException(
			        "Les coaches du résultat ne correspondent pas aux coaches du match."
			    );
			}
		
		if (!coachResultRepository.existsByMatch_Id(matchId)) {
			throw new InvalidResultException(
			        "Aucun résultat à modifier pour le match id " + matchId
			    );
	    }

        validateCoherence(dto.getCoach1Result().getResult(),
                          dto.getCoach2Result().getResult());

        List<CoachResult> existing = coachResultRepository.findAllByMatch_Id(matchId);
        if (existing.size() != 2) {
            throw new InvalidResultException();
        }

        CoachResult existingResult1 = existing.stream()
            .filter(r -> r.getCoach().getId().equals(dto.getCoach1Result().getCoachId()))
            .findFirst()
            .orElseThrow(() -> new CoachNotFoundException(dto.getCoach1Result().getCoachId()));

        CoachResult existingResult2 = existing.stream()
            .filter(r -> r.getCoach().getId().equals(dto.getCoach2Result().getCoachId()))
            .findFirst()
            .orElseThrow(() -> new CoachNotFoundException(dto.getCoach1Result().getCoachId()));

        applyUpdate(existingResult1, dto.getCoach1Result());
        applyUpdate(existingResult2, dto.getCoach2Result());

        coachResultRepository.save(existingResult1);
        coachResultRepository.save(existingResult2);

        return List.of(toDTO(existingResult1), toDTO(existingResult2));
	}

	private Match findMatchOrThrow(Long eventId, Long roundId, Long matchId) {
        return matchRepository.findByIdAndRoundId(matchId, roundId)
            .filter(m -> m.getRound().getEvent().getId().equals(eventId))
            .orElseThrow(() -> new MatchNotFoundException(matchId, roundId));
    }

    private Coach findCoachOrThrow(Long coachId) {
        return coachRepository.findById(coachId)
            .orElseThrow(() -> new CoachNotFoundException(coachId));
    }

    private void validateCoherence(MatchResult result1, MatchResult result2) {
        boolean coherent = switch (result1) {
            case WIN  -> result2 == MatchResult.LOSS;
            case LOSS -> result2 == MatchResult.WIN;
            case DRAW -> result2 == MatchResult.DRAW;
        };
        if (!coherent) {
            throw new InvalidResultException();
        }
    }

    private CoachResult buildCoachResult(CoachResultCreateDTO dto,
                                          Coach coach, Match match) {
        return CoachResult.builder()
            .result(dto.getResult())
            .touchdowns(dto.getTouchdowns())
            .casualties(dto.getCasualties())
            .objectives(dto.getObjectives())
            .bonusObjective(dto.isBonusObjective())
            .passes(dto.getPasses())
            .foulActions(dto.getFoulActions())
            .coach(coach)
            .match(match)
            .build();
    }

    private void applyUpdate(CoachResult entity, CoachResultCreateDTO dto) {
        entity.setResult(dto.getResult());
        entity.setTouchdowns(dto.getTouchdowns());
        entity.setCasualties(dto.getCasualties());
        entity.setObjectives(dto.getObjectives());
        entity.setBonusObjective(dto.isBonusObjective());
        entity.setPasses(dto.getPasses());
        entity.setFoulActions(dto.getFoulActions());
    }

    private CoachResultDTO toDTO(CoachResult entity) {
        CoachResultDTO dto = new CoachResultDTO();
        dto.setId(entity.getId());
        dto.setResult(entity.getResult());
        dto.setTouchdowns(entity.getTouchdowns());
        dto.setCasualties(entity.getCasualties());
        dto.setObjectives(entity.getObjectives());
        dto.setBonusObjective(entity.isBonusObjective());
        dto.setPasses(entity.getPasses());
        dto.setFoulActions(entity.getFoulActions());
        dto.setCoach(coachMapper.toSummaryDTO(entity.getCoach()));
        return dto;
    }
}
