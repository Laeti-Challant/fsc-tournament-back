package com.filsanguinaire.tournament.security;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
// Récupération de l'en-tête Authorization de la requête HTTP
		final String authHeader = request.getHeader("Authorization");
		// Si l'en-tête est absent ou ne commence pas par "Bearer ",
		// on ne tente pas d'authentifier l'utilisateur
		// et on laisse simplement continuer la requête
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		// Extraction du token JWT en supprimant le préfixe "Bearer "
		final String jwt = authHeader.substring(7);
		// Extraction de l'email contenu dans le token
		final String mail = jwtService.extractMail(jwt);
		// On vérifie deux choses :
		// 1. que l'email a bien été extrait du token
		// 2. qu'aucune authentification n'est déjà présente
		// dans le contexte de sécurité
		if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Vérification que le token est valide
			// (signature correcte, non expiré, cohérent avec l'email)
			if (jwtService.isTokenValid(jwt, mail)) {
				// Extraction du rôle depuis le token
				String role = jwtService.extractRole(jwt);
				// Création de l'objet d'authentification Spring Security
				//
				// Paramètres :
				// - principal : ici l'email de l'utilisateur
				// - credentials : null car on n'a pas besoin du mot de passe ici
				// - authorities : liste des rôles/permissions accordés
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(mail, null,
						List.of(new SimpleGrantedAuthority("ROLE_" + role)));
				// Ajout des détails liés à la requête HTTP
				// (adresse IP, session éventuelle, etc.)
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// Enregistrement de l'authentification dans le contexte Spring Security
				// À partir de maintenant, la requête est considérée comme authentifiée
				SecurityContextHolder.getContext().setAuthentication(authToken);
				// Log de debug utile en développement
				log.debug("JWT valide pour : {} avec role : {}", mail, role);
			} else {
				// Si le token n'est pas valide ou a expiré,
				// on journalise l'information
				log.warn("JWT invalide ou expire pour : {}", mail);
			}
		}
		// On poursuit impérativement la chaîne des filtres
		// pour laisser Spring continuer le traitement de la requête
		filterChain.doFilter(request, response);
	}
}
