package com.filsanguinaire.tournament.exceptions;

public class ChallengeAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChallengeAlreadyExistsException(String coachPseudo) {
        super("Le coach " + coachPseudo + " a déjà un match dans ce round");
    }
}
