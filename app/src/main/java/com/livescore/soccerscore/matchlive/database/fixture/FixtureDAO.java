package com.livescore.soccerscore.matchlive.database.fixture;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

@Dao
public interface FixtureDAO {
    @Insert()
    void insert(FixtureModel fixtureBase);

    @Query("DELETE FROM fixture")
    void deleteAll();

    @Query("SELECT * FROM fixture")
    List<FixtureModel> getAllFixture();

    @Query("SELECT * FROM fixture where id = :id")
    FixtureModel getFixtureById(long id);
    @Query("SELECT * FROM fixture where isPin = 1")
    FixtureModel getFixtureByPin();
    @Query("SELECT * FROM fixture WHERE starting_at LIKE '%' || :date || '%'")
    List<FixtureModel> getFixturesByDate(String date);

    @Update
    void update(FixtureModel fixtureBase);

    @Query("Delete from fixture where id = :id")
    void delete(long id);

    @Query("Delete from fixture where isPin = 1")
    void deletePin();

}
