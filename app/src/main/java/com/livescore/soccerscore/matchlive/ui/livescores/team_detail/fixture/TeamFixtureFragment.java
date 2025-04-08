package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture;

import android.content.Intent;
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
import com.livescore.soccerscore.matchlive.databinding.FragmentTeamFixtureBinding;
import com.livescore.soccerscore.matchlive.databinding.FragmentTeamStatsBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamFixtureFragment extends BaseFragment<FragmentTeamFixtureBinding> {
    List<FixtureModel> list = new ArrayList<>();
    FixtureAdapter fixtureAdapter;
    LoadingDialog loadingDialog;

    @Override
    public FragmentTeamFixtureBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTeamFixtureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        fixtureAdapter = new FixtureAdapter(requireContext(), list, new FixtureClickCallBack() {
            @Override
            public void select(int pos, FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "select " + fixtureModel.id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
            }

            @Override
            public void pin(int pos, FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "pin " + fixtureModel.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "alarm " + fixtureModel.name, Toast.LENGTH_SHORT).show();
            }
        });
        long teamId = TeamDetailActivity.instance.teamModel != null ? TeamDetailActivity.instance.teamModel.getId() : 0;
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

    public void startArc(Intent intent) {
        if (getContext() instanceof TeamDetailActivity) {
            TeamDetailActivity main = (TeamDetailActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }
//    public void fetchFixtureScheduleTeam(int teamId) {
//        try {
//            ApiDataService.apiService.callScheduleTeam(teamId, ConstantApiData.KEY,ConstantApiData.TIMEZONE).enqueue(new Callback<SeasonResponse>() {
//                @Override
//                public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
//                        SeasonResponse seasonResponse = response.body();
//                        Log.e("API_RESPONSE", "data: " + seasonResponse.seasons);
//                        if (seasonResponse.seasons != null) {
//                            for (SeasonModel seasonModel : seasonResponse.seasons) {
//                                if (seasonModel != null) {
//                                    for (RoundModel roundModel : seasonModel.rounds) {
//                                        if (roundModel != null) {
//                                            if (roundModel.fixtures != null) {
//                                                for (FixtureModel fixtureModel : roundModel.fixtures) {
//                                                    for (FixtureModel.StateModel stateModel : ConstantApiData.listState) {
//                                                        if (fixtureModel.state_id == stateModel.id) {
//                                                            fixtureModel.setState(stateModel);
//                                                            break;
//                                                        }
//                                                    }
//                                                }
//                                                list.addAll(roundModel.fixtures);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            for (FixtureModel fixtureModel : list) {
//                                Log.e("API_RESPONSE", "fixturesModel: " + fixtureModel);
//
//                            }
//                        }
//                        loadingDialog.dismiss();
//                        binding.rcvFixture.setAdapter(fixtureAdapter);
//                    } else {
//                        loadingDialog.dismiss();
//                        binding.rcvFixture.setAdapter(fixtureAdapter);
//                        Log.e("call_api_data", "call false: Code: " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<SeasonResponse> call, @NonNull Throwable t) {
//                    loadingDialog.dismiss();
//                    binding.rcvFixture.setAdapter(fixtureAdapter);
//                    Log.e("call_api_data", "onfailure" + t);
//                }
//            });
//
//        } catch (Exception e) {
//            loadingDialog.dismiss();
//            binding.rcvFixture.setAdapter(fixtureAdapter);
//            Log.e("call_api_data", "catch: ", e);
//        }
//    }

    public void fetchFixtureTeam(long teamId) {
        try {
            ApiDataService.apiService.callTeamFixture(teamId, ConstantApiData.KEY,ConstantApiData.TIMEZONE, "upcoming.participants;upcoming.scores;upcoming.state").enqueue(new Callback<TeamFixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamFixtureResponse> call, @NonNull Response<TeamFixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON schedule: " + new Gson().toJson(response.body()));
                        TeamFixtureResponse teamFixtureResponse = response.body();
                        Log.e("API_RESPONSE", "data: " + teamFixtureResponse.getData());
                        if (teamFixtureResponse.data != null) {
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
                public void onFailure(@NonNull Call<TeamFixtureResponse> call, @NonNull Throwable t) {
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