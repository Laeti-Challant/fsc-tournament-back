package com.filsanguinaire.tournament.exceptions;

public class TournamentNotEditableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TournamentNotEditableException() {
        super("Le tournoi n'est plus modifiable (statut non PLANNED).");
    }
}
