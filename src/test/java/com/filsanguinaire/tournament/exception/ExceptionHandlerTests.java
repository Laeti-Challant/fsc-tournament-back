package com.filsanguinaire.tournament.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.filsanguinaire.tournament.exceptions.GlobalExceptionHandler;

public class ExceptionHandlerTests {

	private GlobalExceptionHandler handler;
	
	@BeforeEach
	void setUp() {
		handler = new GlobalExceptionHandler();
	}
	
	@Test
	void shouldReturnHttpRequestMethodNotSupportedException() {
		// Arrange
		HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("GET" );
		
		// Act
		ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);
		
		// Assert
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
		assertEquals("Ce chemin n'est pas supporté.", response.getBody().get("message"));
	}
	
}
