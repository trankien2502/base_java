package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_fixture;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentLeagueFixtureBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueFixtureFragment extends BaseFragment<FragmentLeagueFixtureBinding> {
    List<FixtureModel> list = new ArrayList<>();
    FixtureAdapter fixtureAdapter;
    LoadingDialog loadingDialog;

    @Override
    public FragmentLeagueFixtureBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentLeagueFixtureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        fixtureAdapter = new FixtureAdapter(requireContext(), list, new FixtureClickCallBack() {
            @Override
            public void select(FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "select " + fixtureModel.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pin(FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "pin " + fixtureModel.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void alarm(FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "alarm " + fixtureModel.name, Toast.LENGTH_SHORT).show();
            }
        });
        int teamId = LeagueDetailActivity.instance.leagueDetail != null ? LeagueDetailActivity.instance.leagueDetail.getId() : 0;
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            loadingDialog = new LoadingDialog(requireContext(), false);
            loadingDialog.show();
            list.clear();
            fetchFixtureTeam(teamId);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    @Override
    public void bindView() {

    }


    public void fetchFixtureTeam(int teamId) {
        try {
            ApiDataService.apiService.callLeagueFixture(teamId, ConstantApiData.KEY, "upcoming.participants;upcoming.scores;upcoming.state; inplay.participants;inplay.scores;inplay.state").enqueue(new Callback<LeagueFixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<LeagueFixtureResponse> call, @NonNull Response<LeagueFixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON schedule: " + new Gson().toJson(response.body()));
                        LeagueFixtureResponse teamFixtureResponse = response.body();
                        Log.e("API_RESPONSE", "data: " + teamFixtureResponse.getData());
                        if (teamFixtureResponse.data != null) {
                            list.addAll(teamFixtureResponse.getData().inplay);
                            list.addAll(teamFixtureResponse.getData().upcoming);
                        }
                        for (FixtureModel fixtureModel : list) {
                            Log.e("API_RESPONSE", "data: " + fixtureModel);
                        }
                        loadingDialog.dismiss();
                        binding.rcvFixture.setAdapter(fixtureAdapter);
                    } else {
                        loadingDialog.dismiss();
                        binding.rcvFixture.setAdapter(fixtureAdapter);
                        try {
                            Log.e("call_api_data", "call false: Code: " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LeagueFixtureResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    binding.rcvFixture.setAdapter(fixtureAdapter);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            binding.rcvFixture.setAdapter(fixtureAdapter);
            Log.e("call_api_data", "catch: ", e);
        }
    }
}