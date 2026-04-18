package com.filsanguinaire.tournament.exceptions;

public class TournamentFullException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TournamentFullException(int max) {
        super("Le nombre maximum de participants (" + max + ") est atteint.");
    }
}
