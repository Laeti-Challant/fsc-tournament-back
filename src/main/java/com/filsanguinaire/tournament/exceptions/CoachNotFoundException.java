package com.filsanguinaire.tournament.exceptions;

public class CoachNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CoachNotFoundException(Long id) {
        super("Coach introuvable avec l'id : " + id);
    }
}
