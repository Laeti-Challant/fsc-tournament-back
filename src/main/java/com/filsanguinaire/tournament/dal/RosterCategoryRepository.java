package com.filsanguinaire.tournament.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.filsanguinaire.tournament.bo.RosterCategory;

public interface RosterCategoryRepository extends JpaRepository<RosterCategory, Long> {

}
