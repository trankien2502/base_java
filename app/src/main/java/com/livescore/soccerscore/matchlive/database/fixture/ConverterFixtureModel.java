package com.livescore.soccerscore.matchlive.database.fixture;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ConverterFixtureModel {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromTeamInMatchList(List<TeamInMatch> list) {
        if (list == null || list.isEmpty()) {
            return "[]"; // hoặc có thể return null nếu bạn muốn lưu null trong DB
        }
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<TeamInMatch> toTeamInMatchList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList(); // hoặc return null nếu bạn muốn là null
        }
        Type type = new TypeToken<List<TeamInMatch>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String fromScoresList(List<ScoreModel> list) {
        if (list == null || list.isEmpty()) {
            return "[]"; // hoặc có thể return null nếu bạn muốn lưu null trong DB
        }
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<ScoreModel> toScoresList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList(); // hoặc return null nếu bạn muốn là null
        }
        Type type = new TypeToken<List<ScoreModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String fromStateModel(Object state) {
        if (state == null) return null;
        return gson.toJson(state);
    }

    @TypeConverter
    public static Object toStateModel(String json) {
        if (json == null || json.isEmpty()) return null;
        return gson.fromJson(json, Object.class);
    }
}
