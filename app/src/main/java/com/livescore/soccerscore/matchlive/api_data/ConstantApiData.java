package com.livescore.soccerscore.matchlive.api_data;

import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.model.team.TeamInMatch;

import java.util.ArrayList;
import java.util.List;

public class ConstantApiData {
    public static final String LEAGUE = "leagues";
    public static final String TEAM = "teams";
    public static List<TeamInMatch> listTeam = new ArrayList<>();
    public static List<LeagueDetail> listLeague = new ArrayList<>();
    public static List<FixtureModel.StateModel> listState = new ArrayList<>();
    public static final String FIXTURE = "fixtures";
    public static final String STATE = "states";
    public static final String TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final String KEY = "tCaaAbgORG4Czb3byoAN4ywt70oCxMMpfQqVCmRetJp3BYapxRv419koCJQT";


}
