package com.filsanguinaire.tournament.exceptions;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String email) {
		super("Utilisateur introuvable : " + email);
	}
	
	public UserNotFoundException(Long id) {
		super("Utilisateur introuvable avec l'id : " + id);
	}
}
