package com.livescore.soccerscore.matchlive.ui.home.favourite;

import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

public interface LeagueClickCallBack {
    void select(LeagueModel leagueModel);
    void follow(LeagueModel leagueModel);
}
