package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;

public interface LeagueClickCallBack {
    void select(LeagueModel leagueModel);

    void follow(int position, LeagueModel leagueModel);

    void load();
}
