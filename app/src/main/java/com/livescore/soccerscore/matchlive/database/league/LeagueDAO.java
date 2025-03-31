package com.livescore.soccerscore.matchlive.database.league;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;

import java.util.List;

@Dao
public interface LeagueDAO {
    @Insert()
    void insert(LeagueModel teamModel);

    @Query("DELETE FROM league")
    void deleteAll();

    @Query("SELECT * FROM league")
    List<LeagueModel> getAllLeagueFavourite();

    @Query("SELECT * FROM league where isFavourite = :isFavourite")
    List<LeagueModel> getFavouriteTeam(boolean isFavourite);

    @Query("SELECT * FROM league where id = :id")
    LeagueModel getLeagueById(int id);

    @Update
    void update(LeagueModel teamModel);

    @Query("Delete from league where id = :id")
    void delete(int id);

}
