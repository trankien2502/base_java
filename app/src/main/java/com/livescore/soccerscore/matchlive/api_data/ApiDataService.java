package com.livescore.soccerscore.matchlive.api_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livescore.soccerscore.matchlive.api_data.model.StateResponse;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_fixture.LeagueFixtureResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table.StandingResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture.SeasonResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture.TeamFixtureDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture.TeamFixtureResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad.SquadModel;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad.SquadResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDataService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient()
            .create();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    // Thêm interceptor vào OkHttpClient
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging) // Gắn interceptor vào client
            .build();


    ApiDataService apiService = new Retrofit.Builder()
            .baseUrl("https:/api.sportmonks.com/v3/football/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiDataService.class);

    @GET(ConstantApiData.LEAGUE)
    Call<LeagueResponse> callLeague(@Query("api_token") String token, @Query("page") int page, @Query("include") String include);

    @GET(ConstantApiData.STATE)
    Call<StateResponse> callState(@Query("api_token") String token);


    @GET("teams/search/{search}")
    Call<TeamResponse> callTeamSearch(@Path("search") String search,@Query("api_token") String token, @Query("page") int page, @Query("include") String include);
    @GET("leagues/search/{search}")
    Call<LeagueResponse> callLeagueSearch(@Path("search") String search,@Query("api_token") String token, @Query("page") int page, @Query("include") String include);
    @GET(ConstantApiData.TEAM)
    Call<TeamResponse> callTeam(@Query("api_token") String token, @Query("page") int page, @Query("include") String include);

    @GET("leagues/date/{date}")
    Call<FixtureResponse> callFixtureToday(@Path("date") String date, @Query("api_token") String token, @Query("include") String include, @Query("page") int page);

    @GET("squads/teams/{team}")
    Call<SquadResponse> callSquad(@Path("team") int team, @Query("api_token") String token, @Query("include") String include);

    @GET("schedules/teams/{team}")
    Call<SeasonResponse> callScheduleTeam(@Path("team") int team, @Query("api_token") String token);
    @GET("teams/{team}")
    Call<TeamFixtureResponse> callTeamFixture(@Path("team") int team, @Query("api_token") String token, @Query("include") String include);
    @GET("leagues/{league}")
    Call<LeagueFixtureResponse> callLeagueFixture(@Path("league") int league, @Query("api_token") String token, @Query("include") String include);
    @GET("livescores")
    Call<LiveResponse> callLiveMatch(@Query("api_token") String token, @Query("include") String include);
    @GET("standings/live/leagues/{league}")
    Call<StandingResponse> callStandingLeague(@Path("league") int league, @Query("api_token") String token, @Query("include") String include);

}
