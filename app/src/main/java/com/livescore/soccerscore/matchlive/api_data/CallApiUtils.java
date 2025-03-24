package com.livescore.soccerscore.matchlive.api_data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApiUtils {

    public static void callDataLeague(Context context) {
        if (IsNetWork.haveNetworkConnection(context)) {
            try {
                String fixedKey = "ldcyiGDAUEvdBzwVTkbIKcdxDY4Wx8vLYFEBpcksdhDuyA8lMAMkUZIZwEzk";
                ApiDataService.apiService.callLeague(fixedKey).enqueue(new Callback<LeagueResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LeagueResponse> call, @NonNull Response<LeagueResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<LeagueModel> leagues = response.body().getData(); // Lấy danh sách từ `data`
                            Log.e("call_api_data", "call true:");
                            if (leagues != null && !leagues.isEmpty()) {
                                for (LeagueModel leagueModel : leagues) {
                                    Log.e("call_api_data", "data: " + leagueModel.toString());
                                }
                            }
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LeagueResponse> call, @NonNull Throwable t) {
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
    public static void callDataTeam(Context context) {
        if (IsNetWork.haveNetworkConnection(context)) {
            try {
                String fixedKey = "ldcyiGDAUEvdBzwVTkbIKcdxDY4Wx8vLYFEBpcksdhDuyA8lMAMkUZIZwEzk";
                ApiDataService.apiService.callTeam(fixedKey).enqueue(new Callback<TeamResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TeamResponse> call, @NonNull Response<TeamResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<TeamModel> leagues = response.body().getData(); // Lấy danh sách từ `data`
                            Log.e("call_api_data", "call true:");
                            if (leagues != null && !leagues.isEmpty()) {
                                for (TeamModel leagueModel : leagues) {
                                    Log.e("call_api_data", "data: " + leagueModel.toString());
                                }
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
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }


//    public static void callApi(Context context) {
//        SPUtils.setLong(context, SPUtils.CALL_API_TIME_DONE, 0);
//        SPUtils.setBoolean(context, SPUtils.CALL_API_SUCCESS, false);
//        callDataApi(context);
//    }
}
