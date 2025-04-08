package com.livescore.soccerscore.matchlive.database.fixture;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;


@Database(entities = {FixtureModel.class}, version = 1)
@TypeConverters(ConverterFixtureModel.class)
public abstract class FixtureDatabase extends RoomDatabase {
    public abstract FixtureDAO fixtureDAO();

    private static FixtureDatabase instance;

    public static synchronized FixtureDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FixtureDatabase.class, "fixture_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}