package com.filsanguinaire.tournament.exceptions;

public class PseudoAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PseudoAlreadyExistsException(String pseudo) {
		super("Pseuo déjà utilisé : " + pseudo);
	}
}
