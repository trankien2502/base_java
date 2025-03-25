package com.livescore.soccerscore.matchlive.database.league;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;


@Database(entities = {LeagueModel.class}, version = 1)
public abstract class LeagueDatabase extends RoomDatabase {
    public abstract LeagueDAO leagueDAO();

    private static LeagueDatabase instance;

    public static synchronized LeagueDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LeagueDatabase.class, "league_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}