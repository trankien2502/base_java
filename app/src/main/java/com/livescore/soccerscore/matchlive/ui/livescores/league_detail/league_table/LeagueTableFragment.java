package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentLeagueTableBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.live.FixtureLiveModel;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueTableFragment extends BaseFragment<FragmentLeagueTableBinding> {

    LoadingDialog loadingDialog;
    List<StandingModel> list = new ArrayList<>();
    StandingTableAdapter adapter;

    @Override
    public FragmentLeagueTableBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentLeagueTableBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new StandingTableAdapter(requireContext(), list);
        binding.rcvStanding.setAdapter(adapter);
        loadingDialog = new LoadingDialog(requireContext(), false);
        int leagueId = LeagueDetailActivity.instance.leagueDetail != null ? LeagueDetailActivity.instance.leagueDetail.getId() : 0;
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            list.clear();
            loadingDialog.show();
            fetchStanding(leagueId);
        } else {
            Log.e("call_api_data", "No internet to call api");
            new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
        }
    }

    @Override
    public void bindView() {

    }

    public void fetchStanding(int leagueId) {
        try {
            ApiDataService.apiService.callStandingLeague(leagueId, ConstantApiData.KEY, "participant;details.type").enqueue(new Callback<StandingResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<StandingResponse> call, @NonNull Response<StandingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        StandingResponse standingResponse = response.body();
                        if (standingResponse.data != null) {
                            list.addAll(standingResponse.data);
                            Log.e("API_RESPONSE", "data: " + standingResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (StandingModel liveModel : list) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            Collections.sort(list, new Comparator<StandingModel>() {
                                @Override
                                public int compare(StandingModel p1, StandingModel p2) {
                                    return Integer.compare(p1.position, p2.position);
                                }
                            });
                            adapter.notifyDataSetChanged();
                            binding.rcvStanding.post(() -> {
                                loadingDialog.dismiss();
                            });
                        }
                        loadingDialog.dismiss();
                    } else {
                        loadingDialog.dismiss();
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StandingResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            Log.e("call_api_data", "catch: ", e);
        }
    }
}