package com.livescore.soccerscore.matchlive.ui.home.favourite;

import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

public interface TeamClickCallBack {
    void select(TeamModel teamModel);
    void follow(TeamModel teamModel);
}
