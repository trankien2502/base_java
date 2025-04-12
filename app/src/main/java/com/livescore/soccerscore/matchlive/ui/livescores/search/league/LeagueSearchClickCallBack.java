package com.livescore.soccerscore.matchlive.ui.livescores.search.league;

import com.livescore.soccerscore.matchlive.model.league.LeagueModel;

public interface LeagueSearchClickCallBack {
    void select(LeagueModel leagueModel);
    void load();
}
