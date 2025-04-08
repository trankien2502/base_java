package com.livescore.soccerscore.matchlive.api_data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.StateResponse;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueTodayModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApiUtils {

    public static void callDataLeague(Context context) {
        if (IsNetWork.haveNetworkConnection(context)) {
            ConstantApiData.listLeague.clear();
            fetchLeaguePage(1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    public static void callDataState(Context context) {
        if (IsNetWork.haveNetworkConnection(context)) {
            ConstantApiData.listState.clear();
            try {
                ApiDataService.apiService.callState(ConstantApiData.KEY, ConstantApiData.TIMEZONE).enqueue(new Callback<StateResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StateResponse> call, @NonNull Response<StateResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            StateResponse stateResponse = response.body();
                            ConstantApiData.listState.addAll(stateResponse.states);
                            Log.e("call_api_data", "call true:");
                            if (ConstantApiData.listState != null && !ConstantApiData.listState.isEmpty()) {
                                for (FixtureModel.StateModel leagueModel : ConstantApiData.listState) {
                                    Log.e("call_api_data", "data: " + leagueModel.toString());
                                }
                            }
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StateResponse> call, @NonNull Throwable t) {
                        Log.e("call_api_data", "onfailure" + t);
                    }
                });

            } catch (Exception e) {
                Log.e("call_api_data", "catch: ", e);
            }
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    private static void fetchLeaguePage(int page) {
        try {
            ApiDataService.apiService.callLeague(ConstantApiData.KEY, ConstantApiData.TIMEZONE, 1, "country").enqueue(new Callback<LeagueResponse>() {
                @Override
                public void onResponse(@NonNull Call<LeagueResponse> call, @NonNull Response<LeagueResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LeagueResponse leagueResponse = response.body();
                        ConstantApiData.listLeague.addAll(leagueResponse.data); // Lấy danh sách từ `data`
                        Log.e("call_api_data", "call true:");
                        if (ConstantApiData.listLeague != null && !ConstantApiData.listLeague.isEmpty()) {
                            for (LeagueDetail leagueModel : ConstantApiData.listLeague) {
                                Log.e("call_api_data", "data: " + leagueModel.toString());
                            }
                        }
                        Gson gson = new Gson();
                        PaginationModel pagination = gson.fromJson(new Gson().toJson(leagueResponse.pagination), PaginationModel.class);
                        if (pagination.has_more) {
                            fetchLeaguePage(page + 1);
                        } else {
                            Log.e("call_api_data_done", "Total team fetched: " + ConstantApiData.listLeague.size());
                        }
                    } else {
                        Log.e("call_api_data_failed", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LeagueResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data_failed", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data_catch", "catch: ", e);
        }
    }

    public static void callDataTeam(Context context) {
        if (IsNetWork.haveNetworkConnection(context)) {
            ConstantApiData.listTeam.clear();
            fetchTeamPage(1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    private static void fetchTeamPage(int page) {
        try {
            ApiDataService.apiService.callTeam(ConstantApiData.KEY, ConstantApiData.TIMEZONE, page, "country").enqueue(new Callback<TeamResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamResponse> call, @NonNull Response<TeamResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        TeamResponse teamResponse = response.body();
                        ConstantApiData.listTeam.addAll(teamResponse.data);
                        Log.e("call_api_data", "call true:");
                        if (ConstantApiData.listTeam != null && !ConstantApiData.listTeam.isEmpty()) {
                            for (TeamModel leagueModel : ConstantApiData.listTeam) {
                                Log.e("call_api_data", "data: " + leagueModel.toString());
                            }
                        }
                        Gson gson = new Gson();
                        PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                        if (pagination.has_more) {
                            fetchTeamPage(page + 1);
                        } else {
                            Log.e("call_api_data", "Total team fetched: " + ConstantApiData.listTeam.size());
                        }
                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
        }
    }

    public static void fetchFixtureDatePage(String date, int page) {
        try {
            ApiDataService.apiService.callFixtureToday(date, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "today.participants;today.scores;today.state", page).enqueue(new Callback<FixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<FixtureResponse> call, @NonNull Response<FixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        FixtureResponse teamResponse = response.body();
                        Log.e("API_RESPONSE", "data: " + teamResponse.data);
                        for (LeagueTodayModel leagueModel : teamResponse.data) {
                            Log.e("API_RESPONSE", "leagueModel: " + leagueModel);
                            Log.e("API_RESPONSE", "today: " + leagueModel.today.size());
                            for (FixtureModel fixtureModel : leagueModel.today) {
                                Log.e("API_RESPONSE", "fixture: " + fixtureModel.participants);
                                Log.e("API_RESPONSE", "scores: " + fixtureModel.scores);
                                for (ScoreModel scoreModel : fixtureModel.scores) {
                                    Log.e("API_RESPONSE", "score: " + scoreModel);
                                }
                                Log.e("API_RESPONSE", "state: " + fixtureModel.getState());
                            }
                        }
                        Log.e("API_RESPONSE", "pagination: " + teamResponse.pagination);
                        Log.e("call_api_data", "call true:");
                        Gson gson = new Gson();
                        PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                        if (pagination.has_more) {
                            fetchFixtureDatePage(date, page + 1);
                        } else {
                            Log.e("API_RESPONSE", "done ");
                        }
                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FixtureResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
        }
    }

//    public static void callApi(Context context) {
//        SPUtils.setLong(context, SPUtils.CALL_API_TIME_DONE, 0);
//        SPUtils.setBoolean(context, SPUtils.CALL_API_SUCCESS, false);
//        callDataApi(context);
//    }
}
