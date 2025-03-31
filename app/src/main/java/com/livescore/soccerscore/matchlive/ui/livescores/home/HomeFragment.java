package com.livescore.soccerscore.matchlive.ui.livescores.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueTodayModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentHomeBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.search.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    LoadingDialog loadingDialog;
    List<LeagueTodayModel> list = new ArrayList<>();
    LeagueTodayAdapter adapter;

    @Override
    public FragmentHomeBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new LeagueTodayAdapter(requireContext(), list, new LeagueHomeClickCallBack() {
            @Override
            public void select(LeagueTodayModel leagueTodayModel) {
                Toast.makeText(requireContext(), "league: " + leagueTodayModel.name, Toast.LENGTH_SHORT).show();
            }
        });
        binding.rcvLeagueToday.setAdapter(adapter);
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDate = sdf.format(new Date(System.currentTimeMillis()));
            loadingDialog = new LoadingDialog(requireContext(), false);
            loadingDialog.show();
            list.clear();
            fetchFixtureDatePage(selectedDate, 1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }

    }

    @Override
    public void bindView() {
        binding.ivSearch.setOnClickListener(v -> {
            startArc(new Intent(requireContext(), SearchActivity.class));
        });
        binding.btnChooseDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDate = sdf.format(new Date(selection));
                if (IsNetWork.haveNetworkConnection(requireContext())) {
                    list.clear();
                    loadingDialog = new LoadingDialog(requireContext(), false);
                    loadingDialog.show();
                    fetchFixtureDatePage(selectedDate, 1);
                } else {
                    Log.e("call_api_data", "No internet to call api");
                }
            });
        });
    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }

    public void fetchFixtureDatePage(String date, int page) {
        try {
            ApiDataService.apiService.callFixtureToday(date, ConstantApiData.KEY, "today.participants;today.scores;today.state", page).enqueue(new Callback<FixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<FixtureResponse> call, @NonNull Response<FixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        FixtureResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            list.addAll(teamResponse.data);
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
                                loadingDialog.dismiss();
                                binding.rcvLeagueToday.setAdapter(adapter);
                            }
                        } else {
                            loadingDialog.dismiss();
                            binding.rcvLeagueToday.setAdapter(adapter);
                        }
                    } else {
                        loadingDialog.dismiss();
                        binding.rcvLeagueToday.setAdapter(adapter);
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FixtureResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    binding.rcvLeagueToday.setAdapter(adapter);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            binding.rcvLeagueToday.setAdapter(adapter);
            Log.e("call_api_data", "catch: ", e);
        }
    }
}