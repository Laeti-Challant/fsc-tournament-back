package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
