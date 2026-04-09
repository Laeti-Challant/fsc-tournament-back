package com.filsanguinaire.tournament.exceptions;

public class RulesAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RulesAlreadyExistsException(Long eventId) {
        super("Un ruleset existe déjà pour l'event avec l'id : " + eventId);
    }
}
