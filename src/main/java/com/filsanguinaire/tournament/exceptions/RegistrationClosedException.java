package com.filsanguinaire.tournament.exceptions;

public class RegistrationClosedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistrationClosedException() {
        super("Les inscriptions sont fermées pour ce tournoi.");
    }
}
