package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

import com.livescore.soccerscore.matchlive.model.team.TeamModel;

public interface TeamClickCallBack {
    void select(TeamModel teamModel);
    void follow(int position,TeamModel teamModel);
    void load();
}
