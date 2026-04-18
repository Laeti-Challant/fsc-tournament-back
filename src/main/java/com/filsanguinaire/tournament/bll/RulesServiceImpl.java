package com.filsanguinaire.tournament.bll;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.AllowedInducement;
import com.filsanguinaire.tournament.bo.RosterCategory;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.bo.TournamentRules;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dal.TournamentRulesRepository;
import com.filsanguinaire.tournament.dto.rules.AllowedInducementDTO;
import com.filsanguinaire.tournament.dto.rules.RosterCategoryDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.rules.TournamentRulesDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.RulesAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RulesServiceImpl implements IRulesService {

	private final TournamentRulesRepository rulesRepository;
	private final TournamentRepository tournamentRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Optional<TournamentRulesDTO> findByTournament(Long tournamentId) {
	    return rulesRepository.findByTournamentId(tournamentId)
	            .map(this::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public TournamentRulesDTO getByTournament(Long tournamentId) {
		TournamentRules rules = rulesRepository.findByTournamentId(tournamentId)
				.orElseThrow(() -> new EventNotFoundException(tournamentId));
		return toDTO(rules);
	}

	@Override
	@Transactional
	public TournamentRulesDTO create(Long tournamentId, TournamentRulesCreateUpdateDTO dto) {
		Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EventNotFoundException(tournamentId));

		if (rulesRepository.existsByTournamentId(tournamentId)) {
			throw new RulesAlreadyExistsException(tournamentId);
		}

		TournamentRules rules = TournamentRules.builder()
                .tournament(tournament)  // au lieu de .event(event)
                .budgetPo(dto.getBudgetPo())
                .pspPool(dto.getPspPool())
                .maxSkillsPerPlayer(dto.getMaxSkillsPerPlayer())
                .resurrectionMode(dto.getResurrectionMode())
                .mogettePspValue(dto.getMogettePspValue())
                .mogettePoValue(dto.getMogettePoValue())
                .notesText(dto.getNotesText())
                .rosterText(dto.getRosterText())
                .build();

		addInducements(dto.getAllowedInducements(), rules);
		addRosterCategories(dto.getRosterCategories(), rules);

		return toDTO(rulesRepository.save(rules));
	}

	@Override
	@Transactional
	public TournamentRulesDTO update(Long tournamentId, TournamentRulesCreateUpdateDTO dto) {
		TournamentRules rules = rulesRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new EventNotFoundException(tournamentId));

        rules.setBudgetPo(dto.getBudgetPo());
        rules.setPspPool(dto.getPspPool());
        rules.setMaxSkillsPerPlayer(dto.getMaxSkillsPerPlayer());
        rules.setResurrectionMode(dto.getResurrectionMode());
        rules.setMogettePspValue(dto.getMogettePspValue());
        rules.setMogettePoValue(dto.getMogettePoValue());
        rules.setNotesText(dto.getNotesText());
        rules.setRosterText(dto.getRosterText());

        // orphanRemoval = true : on vide les listes, JPA supprime les anciens en base
        rules.getAllowedInducements().clear();
        rules.getRosterCategories().clear();

        addInducements(dto.getAllowedInducements(), rules);
        addRosterCategories(dto.getRosterCategories(), rules);

        return toDTO(rulesRepository.save(rules));
	}

	@Override
	@Transactional
	public TournamentRulesDTO cloneFromTournament(Long targetTournamentId, Long sourceTournamentId) {
		Tournament targetTournament = tournamentRepository.findById(targetTournamentId)
                .orElseThrow(() -> new EventNotFoundException(targetTournamentId));

        if (rulesRepository.existsByTournamentId(targetTournamentId)) {
            throw new RulesAlreadyExistsException(targetTournamentId);
        }

        TournamentRules source = rulesRepository.findByTournamentId(sourceTournamentId)
                .orElseThrow(() -> new EventNotFoundException(sourceTournamentId));

        TournamentRules clone = TournamentRules.builder()
                .tournament(targetTournament)
                .budgetPo(source.getBudgetPo())
                .pspPool(source.getPspPool())
                .maxSkillsPerPlayer(source.getMaxSkillsPerPlayer())
                .resurrectionMode(source.isResurrectionMode())
                .mogettePspValue(source.getMogettePspValue())
                .mogettePoValue(source.getMogettePoValue())
                .notesText(source.getNotesText())
                .rosterText(source.getRosterText())
                .build();

        source.getAllowedInducements().forEach(i ->
            clone.getAllowedInducements().add(
                AllowedInducement.builder()
                        .name(i.getName())
                        .minQty(i.getMinQty())
                        .maxQty(i.getMaxQty())
                        .rules(clone)
                        .build()
            )
        );

        source.getRosterCategories().forEach(c ->
            clone.getRosterCategories().add(
                RosterCategory.builder()
                        .raceName(c.getRaceName())
                        .categoryValue(c.getCategoryValue())
                        .isMinus(c.isMinus())
                        .rules(clone)
                        .build()
            )
        );

        return toDTO(rulesRepository.save(clone));
	}

	private void addInducements(List<AllowedInducementDTO> dtos, TournamentRules rules) {
		if (dtos == null || dtos.isEmpty())
			return;
		dtos.forEach(d -> rules.getAllowedInducements().add(AllowedInducement.builder().name(d.getName())
				.minQty(d.getMinQty()).maxQty(d.getMaxQty()).rules(rules).build()));
	}

	private void addRosterCategories(List<RosterCategoryDTO> dtos, TournamentRules rules) {
		if (dtos == null || dtos.isEmpty())
			return;
		dtos.forEach(d -> rules.getRosterCategories().add(RosterCategory.builder().raceName(d.getRaceName())
				.categoryValue(d.getCategoryValue()).isMinus(d.isMinus()).rules(rules).build()));
	}

	private TournamentRulesDTO toDTO(TournamentRules rules) {
		List<AllowedInducementDTO> inducements = rules.getAllowedInducements().stream().map(i -> AllowedInducementDTO
				.builder().id(i.getId()).name(i.getName()).minQty(i.getMinQty()).maxQty(i.getMaxQty()).build())
				.toList();

		List<RosterCategoryDTO> categories = rules
				.getRosterCategories().stream().map(c -> RosterCategoryDTO.builder().id(c.getId())
						.raceName(c.getRaceName()).categoryValue(c.getCategoryValue()).isMinus(c.isMinus()).build())
				.toList();

		return TournamentRulesDTO.builder()
				.id(rules.getId())
				.budgetPo(rules.getBudgetPo())
				.pspPool(rules.getPspPool())
				.maxSkillsPerPlayer(rules.getMaxSkillsPerPlayer())
				.resurrectionMode(rules.isResurrectionMode())
				.mogettePspValue(rules.getMogettePspValue())
				.mogettePoValue(rules.getMogettePoValue())
				.notesText(rules.getNotesText())
				.rosterText(rules.getRosterText()).allowedInducements(inducements)
				.rosterCategories(categories).build();
	}
}
