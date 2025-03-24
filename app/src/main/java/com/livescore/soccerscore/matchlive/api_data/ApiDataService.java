package com.livescore.soccerscore.matchlive.api_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiDataService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiDataService apiService = new Retrofit.Builder()
            .baseUrl("https:/api.sportmonks.com/v3/football/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiDataService.class);

    @GET(ConstantApiData.LEAGUE)
    Call<LeagueResponse>callLeague(@Query("api_token") String token);
    @GET(ConstantApiData.TEAM)
    Call<TeamResponse>callTeam(@Query("api_token") String token);

}
