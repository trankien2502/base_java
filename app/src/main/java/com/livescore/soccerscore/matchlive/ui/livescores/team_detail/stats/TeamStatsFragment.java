package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.stats;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTeamStatsBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.model.PaginationModel;
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonDetail;
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonResponse;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingDetail;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingModel;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamStatsFragment extends BaseFragment<FragmentTeamStatsBinding> {

    List<SeasonDetail> listSeason = new ArrayList<>();
    List<StandingModel> list = new ArrayList<>();
    LoadingDialog loadingDialog;
    SeasonAdapter adapter;
    long id = 0;

    @Override
    public FragmentTeamStatsBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTeamStatsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        if (TeamDetailActivity.instance != null) {
            if (TeamDetailActivity.instance.teamModel != null) {
                id = TeamDetailActivity.instance.teamModel.getId();
            } else {
                Log.e("call_api_data", "No internet to call api");
            }
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
        adapter = new SeasonAdapter(requireContext(), listSeason, new SeasonClickCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void select(SeasonDetail seasonDetail) {
                binding.viewBlur.setVisibility(GONE);
                binding.cardView.setVisibility(GONE);
                binding.ivExpandDown.setScaleY(1);
                Glide.with(requireContext()).load(seasonDetail.getLeague().image_path).into(binding.ivLeague);
                binding.tvCountry.setText(seasonDetail.getLeague().getCountry().name);
                binding.tvLeague.setText(seasonDetail.getLeague().name + " " + seasonDetail.name);
                loadingDialog.show();
                list.clear();
                fetchStanding(1, seasonDetail.league_id, seasonDetail.id, TeamDetailActivity.instance.teamModel.getId());
            }
        });
        binding.rcvSeason.setAdapter(adapter);
        loadingDialog = new LoadingDialog(requireContext(), false);
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            if (TeamDetailActivity.instance != null) {
                if (TeamDetailActivity.instance.teamModel != null) {
                    loadingDialog.show();
                    listSeason.clear();
                    fetchSeason(TeamDetailActivity.instance.teamModel.getId());
                } else {
                    Log.e("call_api_data", "No internet to call api");
                }
            } else {
                Log.e("call_api_data", "No internet to call api");
            }
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    @Override
    public void bindView() {
        binding.clHeader.setOnClickListener(v -> {
            binding.viewBlur.setVisibility(VISIBLE);
            binding.cardView.setVisibility(VISIBLE);
            binding.ivExpandDown.setScaleY(-1);
        });
        binding.viewBlur.setOnClickListener(v -> {
            binding.viewBlur.setVisibility(GONE);
            binding.cardView.setVisibility(GONE);
            binding.ivExpandDown.setScaleY(1);
        });
    }

    public void fetchSeason(long id) {
        try {
            ApiDataService.apiService.callSeasonOfTeam(id, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "league.country").enqueue(new Callback<SeasonResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<SeasonResponse> call, @NonNull Response<SeasonResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON ss: " + new Gson().toJson(response.body()));
                        SeasonResponse seasonResponse = response.body();
                        if (seasonResponse.data != null) {
                            listSeason.addAll(seasonResponse.data);
                            Log.e("API_RESPONSE", "data: " + seasonResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (SeasonDetail liveModel : listSeason) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            if (!listSeason.isEmpty()) {
                                listSeason.get(0).isSelect = true;
                                SeasonDetail seasonDetail = listSeason.get(0);
                                Glide.with(requireContext()).load(seasonDetail.getLeague().image_path).into(binding.ivLeague);
                                binding.tvCountry.setText(seasonDetail.getLeague().getCountry().name);
                                binding.tvLeague.setText(seasonDetail.getLeague().name + " " + seasonDetail.name);
                                fetchStanding(1, listSeason.get(0).league_id, listSeason.get(0).id, id);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            loadingDialog.dismiss();
                        }
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

        } catch (
                Exception e) {
            loadingDialog.dismiss();
            Log.e("call_api_data", "catch: ", e);
        }
    }

    public void fetchStanding(int page, long leagueId, long seasonId, long teamId) {
        reset();
        try {
            String filters = "standingLeagues:" + leagueId + ";standingdetailTypes:129,130,131,132,133,134,179; standingSeasons:" + seasonId;//
            Log.e("path_api", "path: " + page + " " + leagueId + " " + seasonId + " " + teamId + "\n " + filters);
            ApiDataService.apiService.callStandingLeague(ConstantApiData.KEY, ConstantApiData.TIMEZONE, "participant;details.type", filters, page).enqueue(new Callback<StandingResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<StandingResponse> call, @NonNull Response<StandingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON standing: " + new Gson().toJson(response.body()));
                        StandingResponse standingResponse = response.body();
                        if (standingResponse.data != null) {
                            list.addAll(standingResponse.data);
                            Log.e("API_RESPONSE", "data: " + standingResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (StandingModel liveModel : list) {
                                Log.e("API_RESPONSE", "standing response: " + liveModel.toString());
                            }
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(standingResponse.pagination), PaginationModel.class);
                            if (pagination != null) {
                                if (pagination.has_more) {
                                    fetchStanding(page + 1, leagueId, seasonId, teamId);
                                } else {
                                    for (StandingModel standingModel : list) {
                                        if (standingModel.getParticipant().getId() == teamId) {
                                            for (StandingDetail standingDetail : standingModel.details) {
                                                if (standingDetail.getType().developer_name.equals("OVERALL_CONCEDED"))
                                                    binding.tvGoalsConceded.setText(String.valueOf(standingDetail.value));
                                                if (standingDetail.getType().developer_name.equals("OVERALL_SCORED"))
                                                    binding.tvGoals.setText(String.valueOf(standingDetail.value));
                                                if (standingDetail.getType().developer_name.equals("OVERALL_DRAWS"))
                                                    binding.tvTeamDraws.setText(String.valueOf(standingDetail.value));
                                                if (standingDetail.getType().developer_name.equals("OVERALL_LOST"))
                                                    binding.tvTeamLost.setText(String.valueOf(standingDetail.value));
                                                if (standingDetail.getType().developer_name.equals("OVERALL_WINS"))
                                                    binding.tvTeamWins.setText(String.valueOf(standingDetail.value));
                                            }
                                        }
                                        break;
                                    }
                                    loadingDialog.dismiss();
                                }
                            } else {
                                for (StandingModel standingModel : list) {
                                    if (standingModel.getParticipant().getId() == teamId) {
                                        for (StandingDetail standingDetail : standingModel.details) {
                                            if (standingDetail.getType().developer_name.equals("OVERALL_CONCEDED"))
                                                binding.tvGoalsConceded.setText(String.valueOf(standingDetail.value));
                                            if (standingDetail.getType().developer_name.equals("OVERALL_SCORED"))
                                                binding.tvGoals.setText(String.valueOf(standingDetail.value));
                                            if (standingDetail.getType().developer_name.equals("OVERALL_DRAWS"))
                                                binding.tvTeamDraws.setText(String.valueOf(standingDetail.value));
                                            if (standingDetail.getType().developer_name.equals("OVERALL_LOST"))
                                                binding.tvTeamLost.setText(String.valueOf(standingDetail.value));
                                            if (standingDetail.getType().developer_name.equals("OVERALL_WINS"))
                                                binding.tvTeamWins.setText(String.valueOf(standingDetail.value));
                                        }
                                    }
                                    break;
                                }
                                loadingDialog.dismiss();
                            }
                        } else {

                            loadingDialog.dismiss();
                        }
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

    public void reset() {
        binding.tvGoalsConceded.setText("-");
        binding.tvGoals.setText("-");
        binding.tvTeamDraws.setText("-");
        binding.tvTeamLost.setText("-");
        binding.tvTeamWins.setText("-");
        binding.tvCleansheets.setText("-");
        binding.tvFailed.setText("-");
        binding.tvMostSubstituted.setText("-");
    }
}