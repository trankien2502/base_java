package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.table;

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
import com.livescore.soccerscore.matchlive.model.PaginationModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTableFixtureBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.FixtureDetailModel;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonDetail;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingModel;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table.StandingTableAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFixtureFragment extends BaseFragment<FragmentTableFixtureBinding> {

    LoadingDialog loadingDialog;
    List<StandingModel> list = new ArrayList<>();
    List<SeasonDetail> listSeason = new ArrayList<>();
    public FixtureDetailModel fixtureDetailModel;
    StandingTableAdapter adapter;

    @Override
    public FragmentTableFixtureBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTableFixtureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

        adapter = new StandingTableAdapter(requireContext(), list);
        binding.rcvStanding.setAdapter(adapter);
        loadingDialog = new LoadingDialog(requireContext(), false);
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            list.clear();
            loadingDialog.show();
            if (MatchDetailActivity.instance != null) {
                if (MatchDetailActivity.instance.fixtureDetailModel != null) {
                    fixtureDetailModel = MatchDetailActivity.instance.fixtureDetailModel;
                    //            fetchSeason(1,leagueId);
                    fetchStanding(1, fixtureDetailModel.league_id, fixtureDetailModel.season_id);
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            } else {
                new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
            }
        } else {
            Log.e("call_api_data", "No internet to call api");
            new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
        }
    }

    @Override
    public void bindView() {

    }

    public void fetchStanding(int page, long leagueId, long seasonId) {
        try {
            String filters = "standingLeagues:" + leagueId + ";standingdetailTypes:129,133,134,179; standingSeasons:" + seasonId;
            ApiDataService.apiService.callStandingLeague(ConstantApiData.KEY,ConstantApiData.TIMEZONE, "participant;details.type", filters, page).enqueue(new Callback<StandingResponse>() {
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
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(standingResponse.pagination), PaginationModel.class);
                            if (pagination != null) {
                                if (pagination.has_more) {
                                    fetchStanding(page + 1, leagueId, seasonId);
                                } else {
                                    Collections.sort(list, new Comparator<StandingModel>() {
                                        @Override
                                        public int compare(StandingModel p1, StandingModel p2) {
                                            return Integer.compare(p2.points, p1.points);
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    binding.rcvStanding.post(() -> {
                                        loadingDialog.dismiss();
                                    });
                                }
                            } else {
                                Collections.sort(list, new Comparator<StandingModel>() {
                                    @Override
                                    public int compare(StandingModel p1, StandingModel p2) {
                                        return Integer.compare(p2.points, p1.points);
                                    }
                                });
                                adapter.notifyDataSetChanged();
                                binding.rcvStanding.post(() -> {
                                    loadingDialog.dismiss();
                                });
                            }
                        } else loadingDialog.dismiss();
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