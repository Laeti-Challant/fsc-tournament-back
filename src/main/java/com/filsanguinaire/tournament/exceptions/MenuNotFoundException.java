package com.filsanguinaire.tournament.exceptions;

public class MenuNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuNotFoundException(Long id) {
        super("Menu non trouvé avec l'id : " + id);
    }
}
