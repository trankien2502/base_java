package com.livescore.soccerscore.matchlive.ui.livescores.home;

import com.livescore.soccerscore.matchlive.model.league.LeagueTodayModel;

public interface LeagueHomeClickCallBack {
    void select(LeagueTodayModel leagueTodayModel);
    void load();

}
