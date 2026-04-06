package com.filsanguinaire.tournament.security;

import java.io.IOException;
import java.util.Arrays;
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
import jakarta.servlet.http.Cookie;
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

	    // Récupération du token depuis le cookie HttpOnly
	    String jwt = null;
	    if (request.getCookies() != null) {
	        jwt = Arrays.stream(request.getCookies())
	                .filter(c -> c.getName().equals("jwt"))
	                .map(Cookie::getValue)
	                .findFirst()
	                .orElse(null);
	    }

	    // Si pas de cookie jwt, on laisse continuer la requête
	    if (jwt == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    // Extraction de l'email contenu dans le token
	    final String mail = jwtService.extractMail(jwt);

	    if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        if (jwtService.isTokenValid(jwt, mail)) {
	            String role = jwtService.extractRole(jwt);
	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(mail, null,
	                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            log.debug("JWT valide pour : {} avec role : {}", mail, role);
	        } else {
	            log.warn("JWT invalide ou expire pour : {}", mail);
	        }
	    }
	    filterChain.doFilter(request, response);
	}
}
