package com.livescore.soccerscore.matchlive.api_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livescore.soccerscore.matchlive.model.StateResponse;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.model.team.TeamResponse;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.FixtureDetailResponse;
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonResponse;
import com.livescore.soccerscore.matchlive.model.league.detail.LeagueFixtureResponse;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingResponse;
import com.livescore.soccerscore.matchlive.model.live.LiveResponse;
import com.livescore.soccerscore.matchlive.model.fixture.TeamFixtureResponse;
import com.livescore.soccerscore.matchlive.model.fixture.squad.SquadResponse;

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
    Call<LeagueResponse> callLeague( @Query("api_token") String token,@Query("timezone") String timezone, @Query("page") int page, @Query("include") String include);

    @GET(ConstantApiData.STATE)
    Call<StateResponse> callState( @Query("api_token") String token,@Query("timezone") String timezone);


    @GET("teams/search/{search}")
    Call<TeamResponse> callTeamSearch(@Path("search") String search, @Query("api_token") String token, @Query("timezone") String timezone, @Query("page") int page, @Query("include") String include);

    @GET("leagues/search/{search}")
    Call<LeagueResponse> callLeagueSearch(@Path("search") String search, @Query("api_token") String token, @Query("timezone") String timezone, @Query("page") int page, @Query("include") String include);

    @GET(ConstantApiData.TEAM)
    Call<TeamResponse> callTeam( @Query("api_token") String token,@Query("timezone") String timezone, @Query("page") int page, @Query("include") String include);

    @GET("leagues/date/{date}")
    Call<FixtureResponse> callFixtureToday(@Path("date") String date, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include, @Query("page") int page);

    @GET("squads/teams/{team}")
    Call<SquadResponse> callSquad(@Path("team") long team, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include);

    @GET("schedules/teams/{team}")
    Call<SeasonResponse> callScheduleTeam(@Path("team") long team, @Query("api_token") String token, @Query("timezone") String timezone);

    @GET("teams/{team}")
    Call<TeamFixtureResponse> callTeamFixture(@Path("team") long team, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include);

    @GET("leagues/{league}")
    Call<LeagueFixtureResponse> callLeagueFixture(@Path("league") long league, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include);

    @GET("livescores/inplay")
    Call<LiveResponse> callLiveMatch( @Query("api_token") String token,@Query("timezone") String timezone, @Query("include") String include);

    @GET("standings")
    Call<StandingResponse> callStandingLeague( @Query("api_token") String token,@Query("timezone") String timezone, @Query("include") String include, @Query("filters") String filter, @Query("page") int page);

    @GET("fixtures/{fixtureId}")
    Call<FixtureDetailResponse> callFixtureDetail(@Path("fixtureId") long id, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include, @Query("filters") String filters);
    @GET("fixtures/{fixtureId}")
    Call<FixtureDetailResponse> callFixtureDetail(@Path("fixtureId") long id, @Query("api_token") String token, @Query("timezone") String timezone, @Query("include") String include);
    @GET("seasons")
    Call<SeasonResponse> callSeasonOfLeague( @Query("api_token") String token,@Query("timezone") String timezone, @Query("include") String include, @Query("filters") String filter, @Query("page") int page);

    @GET("seasons/teams/{teamId}")
    Call<SeasonResponse> callSeasonOfTeam(@Path("teamId") long id, @Query("api_token") String token,@Query("timezone") String timezone, @Query("include") String include);
}
