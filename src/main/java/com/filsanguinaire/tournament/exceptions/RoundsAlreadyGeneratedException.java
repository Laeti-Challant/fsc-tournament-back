package com.filsanguinaire.tournament.exceptions;

public class RoundsAlreadyGeneratedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoundsAlreadyGeneratedException(Long eventId) {
        super("Les rounds ont déjà été générés pour l'event : " + eventId);
    }
}
