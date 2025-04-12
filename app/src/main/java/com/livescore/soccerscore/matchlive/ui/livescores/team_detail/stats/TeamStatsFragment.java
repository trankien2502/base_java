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
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonDetail;
import com.livescore.soccerscore.matchlive.model.fixture.stats.SeasonResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamStatsFragment extends BaseFragment<FragmentTeamStatsBinding> {

    List<SeasonDetail> listSeason = new ArrayList<>();
    LoadingDialog loadingDialog;
    SeasonAdapter adapter;

    @Override
    public FragmentTeamStatsBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTeamStatsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
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
            ApiDataService.apiService.callSeasonOfTeam(id, ConstantApiData.KEY,ConstantApiData.TIMEZONE, "league.country").enqueue(new Callback<SeasonResponse>() {
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
//                            Collections.sort(listSeason, new Comparator<SeasonDetail>() {
//                                @SuppressLint("SimpleDateFormat")
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//                                @Override
//                                public int compare(SeasonDetail m1, SeasonDetail m2) {
//                                    try {
//                                        Date date1 = sdf.parse(m1.ending_at);
//                                        Date date2 = sdf.parse(m2.ending_at);
//                                        return date2.compareTo(date1); // Sắp xếp giảm dần (mới nhất trước)
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                        return 0;
//                                    }
//                                }
//                            });
                            if (!listSeason.isEmpty()) {
                                listSeason.get(0).isSelect = true;
                                SeasonDetail seasonDetail = listSeason.get(0);
                                Glide.with(requireContext()).load(seasonDetail.getLeague().image_path).into(binding.ivLeague);
                                binding.tvCountry.setText(seasonDetail.getLeague().getCountry().name);
                                binding.tvLeague.setText(seasonDetail.getLeague().name + " " + seasonDetail.name);
                            }
                            adapter.notifyDataSetChanged();
                            binding.rcvSeason.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismiss();
                                }
                            });
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
}