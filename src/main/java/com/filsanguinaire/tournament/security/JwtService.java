package com.filsanguinaire.tournament.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class JwtService {
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long jwtExpiration;

	public String generateToken(String mail, String role) {
		// Map des claims personnalisés à intégrer au token
		Map<String, Object> extraClaims = new HashMap<>();
		// Ajout du rôle dans les claims
		extraClaims.put("role", role);
		// Log de debug utile en développement
		log.debug("Génération JWT pour : {}", mail);
		// Construction finale du token
		return buildToken(extraClaims, mail);
	}

	private String buildToken(Map<String, Object> extraClaims, String subject) {
		return Jwts.builder()
				// Ajout des claims personnalisés
				.claims(extraClaims)
				// Définition du sujet principal du token
				.subject(subject)
				// Date d'émission du token
				.issuedAt(new Date(System.currentTimeMillis()))
				// Date d'expiration du token
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				// Signature du token avec la clé secrète
				.signWith(getSigningKey())
				// Génération finale au format String
				.compact();
	}

	public boolean isTokenValid(String token, String mail) {
		// Extraction de l'email contenu dans le token
		final String extractedMail = extractMail(token);
		// Vérifie la correspondance de l'email
		// ainsi que la non-expiration du token
		return extractedMail.equals(mail) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		// Compare la date d'expiration du token avec la date actuelle
		return extractExpiration(token).before(new Date());
	}

	public String extractMail(String token) {
		// Le sujet du token correspond à l'email
		return extractClaim(token, Claims::getSubject);
	}

	public String extractRole(String token) {
		// Lecture du claim personnalisé "role"
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	private Date extractExpiration(String token) {
		// Le claim standard Expiration est lu ici
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		// Applique la fonction d'extraction sur l'ensemble des claims
		return claimsResolver.apply(extractAllClaims(token));
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				// Vérification de la signature avec la clé secrète
				.verifyWith(getSigningKey())
				// Construction du parser
				.build()
				// Parsing du token signé
				.parseSignedClaims(token)
				// Récupération du payload = claims
				.getPayload();
	}

	private SecretKey getSigningKey() {
		// Décodage de la clé Base64 en tableau de bytes
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		// Création de la clé HMAC à partir des bytes décodés
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
