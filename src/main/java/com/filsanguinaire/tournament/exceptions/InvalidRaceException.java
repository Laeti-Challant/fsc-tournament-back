package com.filsanguinaire.tournament.exceptions;

public class InvalidRaceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRaceException(String race) {
        super("La race '" + race + "' n'est pas autorisée pour ce tournoi.");
    }
}
