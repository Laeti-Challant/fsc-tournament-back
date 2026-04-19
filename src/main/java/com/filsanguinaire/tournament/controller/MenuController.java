package com.filsanguinaire.tournament.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filsanguinaire.tournament.bll.IMenuService;
import com.filsanguinaire.tournament.dto.menu.MenuCreateUpdateDTO;
import com.filsanguinaire.tournament.dto.menu.MenuDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/tournaments/{tournamentId}/menus")
@RequiredArgsConstructor
public class MenuController {

	private final IMenuService menuService;
	
	@GetMapping
    public ResponseEntity<List<MenuDTO>> getByTournament(@PathVariable("tournamentId") Long tournamentId) {
        return ResponseEntity.ok(menuService.getByTournament(tournamentId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> create(
            @PathVariable("tournamentId") Long tournamentId,
            @RequestBody @Valid MenuCreateUpdateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuService.create(tournamentId, dto));
    }

    @PutMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> update(
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("menuId") Long menuId,
            @RequestBody @Valid MenuCreateUpdateDTO dto) {
        return ResponseEntity.ok(menuService.update(tournamentId, menuId, dto));
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable("tournamentId") Long tournamentId,
            @PathVariable("menuId") Long menuId) {
        menuService.delete(tournamentId, menuId);
        return ResponseEntity.noContent().build();
    }
}
