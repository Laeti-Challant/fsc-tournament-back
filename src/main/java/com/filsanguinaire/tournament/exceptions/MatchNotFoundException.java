package com.filsanguinaire.tournament.exceptions;

public class MatchNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MatchNotFoundException(Long id, Long roundId) {
        super("Match non trouvé avec l'id : " + id + " pour le round : " + roundId);
    }

    public MatchNotFoundException(Long id) {
        super("Match non trouvé avec l'id : " + id);
    }
}
