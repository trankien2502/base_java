package com.livescore.soccerscore.matchlive.database.team;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;


@Database(entities = {TeamModel.class}, version = 1)
public abstract class TeamDatabase extends RoomDatabase {
    public abstract TeamDAO teamDAO();

    private static TeamDatabase instance;

    public static synchronized TeamDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TeamDatabase.class, "team_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}