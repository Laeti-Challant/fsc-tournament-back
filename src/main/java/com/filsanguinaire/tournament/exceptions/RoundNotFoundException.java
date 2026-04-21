package com.filsanguinaire.tournament.exceptions;

public class RoundNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RoundNotFoundException(Long id, Long eventId) {
        super("Round non trouvé avec l'id : " + id + " pour l'event : " + eventId);
    }

    public RoundNotFoundException(Long id) {
        super("Round non trouvé avec l'id : " + id);
    }
}
