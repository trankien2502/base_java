package com.livescore.soccerscore.matchlive.api_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;

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
    Call<LeagueResponse> callLeague(@Query("api_token") String token, @Query("page") int page);

    @GET(ConstantApiData.TEAM)
    Call<TeamResponse> callTeam(@Query("api_token") String token, @Query("page") int page);
    @GET("leagues/date/{date}")
    Call<FixtureResponse> callFixtureToday(@Path("date") String date, @Query("api_token") String token, @Query("include") String include, @Query("page") int page);

}
