package com.filsanguinaire.tournament.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);
	
	boolean existsByPseudo(String pseudo);
	
	Optional<User> findByEmail(String email);
}
