package com.livescore.soccerscore.matchlive.ui.livescores.search.team;

import com.livescore.soccerscore.matchlive.model.team.TeamModel;

public interface TeamSearchClickCallBack {
    void select(TeamModel teamModel);
    void load();
}
