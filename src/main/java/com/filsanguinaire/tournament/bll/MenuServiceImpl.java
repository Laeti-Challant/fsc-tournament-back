package com.filsanguinaire.tournament.bll;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filsanguinaire.tournament.bo.Menu;
import com.filsanguinaire.tournament.bo.Tournament;
import com.filsanguinaire.tournament.dal.MenuRepository;
import com.filsanguinaire.tournament.dal.TournamentRepository;
import com.filsanguinaire.tournament.dto.menu.MenuCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.menu.MenuDTO;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.MenuNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements IMenuService {

	private final MenuRepository menuRepository;
    private final TournamentRepository tournamentRepository;
    
	@Override
	@Transactional(readOnly = true)
	public List<MenuDTO> getByTournament(Long tournamentId) {
		if (!tournamentRepository.existsById(tournamentId)) {
            throw new EventNotFoundException(tournamentId);
        }
        return menuRepository.findByTournamentId(tournamentId)
                .stream()
                .map(this::toDTO)
                .toList();
	}

	@Override
	@Transactional
	public MenuDTO create(Long tournamentId, MenuCreateUpdateDTO dto) {
		Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EventNotFoundException(tournamentId));

        Menu menu = Menu.builder()
                .label(dto.getLabel())
                .description(dto.getDescription())
                .tournament(tournament)
                .build();

        return toDTO(menuRepository.save(menu));
	}

	@Override
	@Transactional
	public MenuDTO update(Long tournamentId, Long menuId, MenuCreateUpdateDTO dto) {
		Menu menu = menuRepository.findById(menuId)
                .filter(m -> m.getTournament().getId().equals(tournamentId))
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        menu.setLabel(dto.getLabel());
        menu.setDescription(dto.getDescription());

        return toDTO(menuRepository.save(menu));
	}

	@Override
	@Transactional
	public void delete(Long tournamentId, Long menuId) {
		Menu menu = menuRepository.findById(menuId)
                .filter(m -> m.getTournament().getId().equals(tournamentId))
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        menuRepository.delete(menu);
	}
	
	private MenuDTO toDTO(Menu menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .description(menu.getDescription())
                .build();
    }

}
