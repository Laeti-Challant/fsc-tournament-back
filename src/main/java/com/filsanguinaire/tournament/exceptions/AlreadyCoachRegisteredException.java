package com.filsanguinaire.tournament.exceptions;

public class AlreadyCoachRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyCoachRegisteredException() {
        super("Vous êtes déjà inscrit à cet événement.");
    }
}
