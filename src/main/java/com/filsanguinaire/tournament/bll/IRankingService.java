package com.filsanguinaire.tournament.bll;

import com.filsanguinaire.tournament.dto.ranking.RankingsDTO;

public interface IRankingService {

	RankingsDTO getRankings(Long tournamentId);
}
