package com.filsanguinaire.tournament.bll;

import java.util.List;

import com.filsanguinaire.tournament.dto.menu.MenuCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.menu.MenuDTO;

public interface IMenuService {

	List<MenuDTO> getByTournament(Long tournamentId);
	
    MenuDTO create(Long tournamentId, MenuCreateUpdateDTO dto);
    
    MenuDTO update(Long tournamentId, Long menuId, MenuCreateUpdateDTO dto);
    
    void delete(Long tournamentId, Long menuId);
}
