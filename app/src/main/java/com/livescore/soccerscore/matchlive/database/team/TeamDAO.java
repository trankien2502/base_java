package com.livescore.soccerscore.matchlive.database.team;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

import java.util.List;

@Dao
public interface TeamDAO {
    @Insert()
    void insert(TeamModel teamModel);

    @Query("DELETE FROM team")
    void deleteAll();

    @Query("SELECT * FROM team")
    List<TeamModel> getAllTeamFavourite();

    @Query("SELECT * FROM team where isFavourite = :isFavourite")
    List<TeamModel> getFavouriteTeam(boolean isFavourite);

    @Query("SELECT * FROM team where id = :id")
    TeamModel getIconById(int id);

    @Update
    void update(TeamModel teamModel);

    @Query("Delete from team where id = :id")
    void delete(int id);

}
