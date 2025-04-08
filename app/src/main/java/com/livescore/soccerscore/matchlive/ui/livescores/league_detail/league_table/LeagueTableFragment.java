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
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentLeagueTableBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats.SeasonDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats.SeasonResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;

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
    List<SeasonDetail> listSeason = new ArrayList<>();
    StandingTableAdapter adapter;
    boolean isHaveSeasonCurrent = false;

    @Override
    public FragmentLeagueTableBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentLeagueTableBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new StandingTableAdapter(requireContext(), list);
        binding.rcvStanding.setAdapter(adapter);
        loadingDialog = new LoadingDialog(requireContext(), false);
        long leagueId = LeagueDetailActivity.instance.leagueDetail != null ? LeagueDetailActivity.instance.leagueDetail.getId() : 0;
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            list.clear();
            loadingDialog.show();
            fetchSeason(1, leagueId);
//            fetchStanding(1, leagueId, 23619);
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
            ApiDataService.apiService.
                    callStandingLeague(ConstantApiData.KEY,ConstantApiData.TIMEZONE, "participant;details.type", filters, page).enqueue(new Callback<StandingResponse>() {
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

    public void fetchSeason(int page, long leagueId) {
        try {
            String filters = "seasonLeagues:" + leagueId;
            ApiDataService.apiService.callSeasonOfLeague(ConstantApiData.KEY,ConstantApiData.TIMEZONE, "league.country", filters, page).enqueue(new Callback<SeasonResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        SeasonResponse seasonResponse = response.body();
                        if (seasonResponse.data != null) {
                            listSeason.addAll(seasonResponse.data);
                            Log.e("API_RESPONSE", "data: " + seasonResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (SeasonDetail liveModel : listSeason) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(seasonResponse.pagination), PaginationModel.class);
                            if (pagination != null) {
                                if (pagination.has_more) {
                                    fetchSeason(page + 1, leagueId);
                                } else {
                                    for (SeasonDetail seasonDetail : listSeason) {
                                        if (seasonDetail.is_current) {
                                            isHaveSeasonCurrent = true;
                                            fetchStanding(1, leagueId, seasonDetail.id);
                                            break;
                                        }
                                    }
                                    if (!isHaveSeasonCurrent) loadingDialog.dismiss();
                                }
                            } else {
                                for (SeasonDetail seasonDetail : listSeason) {
                                    if (seasonDetail.is_current) {
                                        isHaveSeasonCurrent = true;
                                        fetchStanding(1, leagueId, seasonDetail.id);
                                        break;
                                    }
                                }
                                if (!isHaveSeasonCurrent) loadingDialog.dismiss();
                            }
                        } else loadingDialog.dismiss();
                    } else {
                        loadingDialog.dismiss();
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SeasonResponse> call, @NonNull Throwable t) {
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