package com.filsanguinaire.tournament.exceptions;

public class ResultAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ResultAlreadyExistsException(Long matchId) {
        super("Des résultats existent déjà pour le match id " + matchId);
    }
}
