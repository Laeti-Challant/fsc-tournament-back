package com.filsanguinaire.tournament.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Utilisateur introuvable
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
 // 404 - Event introuvable
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    // 404 - Coach introuvable
    @ExceptionHandler(CoachNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCoachNotFound(CoachNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    // 404 - Menu introuvable
    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMenuNotFound(MenuNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    // 404 - Round introuvable
    @ExceptionHandler(RoundNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoundNotFound(RoundNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 404 - Match introuvable
    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMatchNotFound(MatchNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 - Coach déjà présent dans ce round (défi)
    @ExceptionHandler(ChallengeAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleChallengeAlreadyExists(ChallengeAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409 - Ruleset déjà existant pour cet event
    @ExceptionHandler(RulesAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRulesAlreadyExists(RulesAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409 - Email déjà utilisé
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409 - Pseudo déjà utilisé
    @ExceptionHandler(PseudoAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePseudoAlreadyExists(PseudoAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    // 409 - Coach déjà existant pour cet event
    @ExceptionHandler(AlreadyCoachRegisteredException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyRegistered(AlreadyCoachRegisteredException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    // 409 - Tournoi non éditable 
    @ExceptionHandler(TournamentNotEditableException.class)
    public ResponseEntity<Map<String, Object>> handleTournamentNoteditable(TournamentNotEditableException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409 - Le tournoi est complet
    @ExceptionHandler(TournamentFullException.class)
    public ResponseEntity<Map<String, Object>> handleTournamentFull(TournamentFullException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    // 409 - Rounds déjà générés pour cet event
    @ExceptionHandler(RoundsAlreadyGeneratedException.class)
    public ResponseEntity<Map<String, Object>> handleRoundsAlreadyGenerated(RoundsAlreadyGeneratedException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    
    // 409 - result du match existent déja
    @ExceptionHandler(ResultAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResultAlreadyExists(ResultAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 401 — Mauvais identifiants
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
    }

    // 403 - Compte désactivé
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Compte désactivé");
    }
    // 403 - Autorisation refusée
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Accès refusé");
    }
    
    // 403 - Opération illégale
    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalOperation(IllegalOperationException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }
    
    // 400 - Erreurs de validation @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        // On collecte tous les messages d'erreur de validation
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    // 400 - données de race incorrectes
    @ExceptionHandler(InvalidRaceException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRace(InvalidRaceException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    // 400 - Appariement impossible (nombre impair, doublon...)
    @ExceptionHandler(PairingException.class)
    public ResponseEntity<Map<String, Object>> handlePairingException(PairingException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    // 400 - Coach non validé
    @ExceptionHandler(CoachNotValidatedException.class)
    public ResponseEntity<Map<String, Object>> handleCoachNotValidated(CoachNotValidatedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    // 400 - résultats incohérents
    @ExceptionHandler(InvalidResultException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidResult(InvalidResultException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 404 - Route inexistante
    @ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(Exception ex) {
        log.warn("Route inconnue : {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Ressource introuvable");
    }

    // 500 - Erreur inattendue
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Erreur non gérée", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue s'est produite");
    }

    // Méthode utilitaire pour construire une réponse d'erreur cohérente
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}