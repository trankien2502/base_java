package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTeamSquadBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.model.fixture.squad.SquadModel;
import com.livescore.soccerscore.matchlive.model.fixture.squad.SquadResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamSquadFragment extends BaseFragment<FragmentTeamSquadBinding> {

    LoadingDialog loadingDialog;
    List<SquadModel> squadModelList = new ArrayList<>();
    SquadAdapter adapter;
    int count = 0;

    @Override
    public FragmentTeamSquadBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTeamSquadBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new SquadAdapter(requireContext(), squadModelList);

    }

    @Override
    public void onResume() {
        super.onResume();
        long teamId = TeamDetailActivity.instance.teamModel != null ? TeamDetailActivity.instance.teamModel.getId() : 0;

        if (IsNetWork.haveNetworkConnection(requireContext()) && count == 0) {
            loadingDialog = new LoadingDialog(requireContext(), false);
            loadingDialog.show();
            squadModelList.clear();
            fetchSquadPage(teamId);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    @Override
    public void bindView() {

    }

    public void fetchSquadPage(long teamId) {
        count++;
        try {
            ApiDataService.apiService.callSquad(teamId, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "player;position").enqueue(new Callback<SquadResponse>() {
                @Override
                public void onResponse(@NonNull Call<SquadResponse> call, @NonNull Response<SquadResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON squad: " + new Gson().toJson(response.body()));
                        SquadResponse squadResponse = response.body();
                        Log.e("API_RESPONSE", "squadResponse: " + squadResponse);

                        if (squadResponse.data != null)
                            squadModelList.addAll(squadResponse.data);
//                        Gson gson = new Gson();
//                        PaginationModel pagination = gson.fromJson(new Gson().toJson(squadResponse.pagination), PaginationModel.class);
//                        if (pagination.has_more) {
//                        } else {
//                            loadingDialog.dismiss();
//                            binding.rcvSquad.setAdapter(adapter);
//                        }
                        loadingDialog.dismiss();
                        binding.rcvSquad.setAdapter(adapter);
                        if (squadModelList.isEmpty()) {
                            binding.noData.setVisibility(VISIBLE);
                        } else binding.noData.setVisibility(GONE);
                    } else {
                        loadingDialog.dismiss();
                        if (squadModelList.isEmpty()) {
                            binding.noData.setVisibility(VISIBLE);
                        } else binding.noData.setVisibility(GONE);
                        binding.rcvSquad.setAdapter(adapter);
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SquadResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    if (squadModelList.isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                    } else binding.noData.setVisibility(GONE);
                    binding.rcvSquad.setAdapter(adapter);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            if (squadModelList.isEmpty()) {
                binding.noData.setVisibility(VISIBLE);
            } else binding.noData.setVisibility(GONE);
            binding.rcvSquad.setAdapter(adapter);
            Log.e("call_api_data", "catch: ", e);
        }
    }
}