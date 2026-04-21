package com.filsanguinaire.tournament.exceptions;

public class InvalidResultException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	public InvalidResultException() {
        super("Les résultats sont incohérents : WIN doit être opposé à LOSS, DRAW doit être symétrique.");
    }
}
