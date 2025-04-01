package com.livescore.soccerscore.matchlive.ui.livescores.search.league;

import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

public interface LeagueSearchClickCallBack {
    void select(LeagueModel leagueModel);
    void load();
}
