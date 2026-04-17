package com.filsanguinaire.tournament.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	
	@Value("${app.cors.allowed-origins}")
	private String allowedOrigins;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())

				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex
				        .authenticationEntryPoint((request, response, authException) -> 
				            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non authentifié")
				        )
				    )
				.authorizeHttpRequests(auth -> auth
					    // Swagger
					    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					    // Auth publique
					    .requestMatchers("/auth/**").permitAll()
					    // /coaches/me nécessite authentification
					    .requestMatchers(HttpMethod.GET, "/tournaments/*/coaches/me").authenticated()
					    // Inscription nécessite authentification
					    .requestMatchers(HttpMethod.POST, "/tournaments/*/coaches").authenticated()
					    // Modification inscription nécessite authentification
					    .requestMatchers(HttpMethod.PUT, "/tournaments/*/coaches/me").authenticated()
					    // Lecture publique des events
					    .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
					    // Lecture publique des tournaments (current, coaches list)
					    .requestMatchers(HttpMethod.GET, "/tournaments/**").permitAll()
					    // Lecture publique des classements
					    .requestMatchers(HttpMethod.GET, "/standings/**").permitAll()
					    // Tout le reste nécessite authentification
					    .anyRequest().authenticated()
					)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		// Création d'une nouvelle configuration CORS
		CorsConfiguration config = new CorsConfiguration();

		// Origines autorisées à appeler l'API
		config.setAllowedOrigins(List.of(allowedOrigins));

		// Méthodes HTTP autorisées
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		// En-têtes HTTP autorisés
		config.setAllowedHeaders(List.of("*"));

		// Autorise l'envoi de credentials
		// (cookies, Authorization headers, etc.)
		config.setAllowCredentials(true);

		// Association de cette configuration à toutes les routes de l'application
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}