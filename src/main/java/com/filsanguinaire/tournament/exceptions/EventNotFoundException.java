package com.filsanguinaire.tournament.exceptions;

public class EventNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EventNotFoundException(Long id) {
        super("Event non trouvé avec l'id : " + id);
    }
}
