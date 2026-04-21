package com.filsanguinaire.tournament.exceptions;

public class CoachNotValidatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CoachNotValidatedException(String coachPseudo) {
        super("Le coach " + coachPseudo + " n'est pas validé");
    }
}
