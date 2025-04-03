package com.livescore.soccerscore.matchlive.ui.livescores.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.R;
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
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.live.FixtureLiveModel;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveMatchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveMatchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveScoreActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.search.SearchActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

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
    List<FixtureLiveModel> listLive = new ArrayList<>();
    LeagueTodayAdapter adapter;
    LiveMatchAdapter liveMatchAdapter;
    int currentPage = 1;
    String selectedDate = "";
    boolean isEnableToLoadMore = true;

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

            @Override
            public void load() {
                loadingDialog = new LoadingDialog(requireContext(), false);
                loadingDialog.show();
                if (isEnableToLoadMore) {
                    currentPage++;
                    fetchFixtureDatePage(selectedDate, currentPage);
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        }, new FixtureClickCallBack() {
            @Override
            public void select(FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "select " + fixtureModel.id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
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
        liveMatchAdapter = new LiveMatchAdapter(requireContext(), listLive, new LiveMatchClickCallBack() {
            @Override
            public void detail(FixtureLiveModel fixtureModel) {
                Toast.makeText(requireContext(), "select " + fixtureModel.id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
            }
        });
        binding.rcvLeagueToday.setAdapter(adapter);
        binding.rcvLive.setAdapter(liveMatchAdapter);
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(new Date(System.currentTimeMillis()));
            loadingDialog = new LoadingDialog(requireContext(), false);
            loadingDialog.show();
            list.clear();
            currentPage = 1;
            fetchFixtureDatePage(selectedDate, 1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            listLive.clear();
            binding.loadingLive.setVisibility(VISIBLE);
            fetchLiveMatch();
        } else {
            Log.e("call_api_data", "No internet to call api");
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void bindView() {
        binding.ivReload.setOnClickListener(v -> {
            if (IsNetWork.haveNetworkConnection(requireContext())) {
                listLive.clear();
                liveMatchAdapter.notifyDataSetChanged();
                binding.loadingLive.setVisibility(VISIBLE);
                fetchLiveMatch();
            } else {
                Log.e("call_api_data", "No internet to call api");
            }
        });
        binding.tvSeeMore.setOnClickListener(v -> {
            startArc(new Intent(requireContext(), LiveScoreActivity.class));
        });
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
                selectedDate = sdf.format(new Date(selection));
                if (IsNetWork.haveNetworkConnection(requireContext())) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    loadingDialog = new LoadingDialog(requireContext(), false);
                    loadingDialog.show();
                    currentPage = 1;
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
                        Log.e("API_RESPONSE", "page: " + page);
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        FixtureResponse teamResponse = response.body();
                        int oldPos = list.size();
                        if (teamResponse.data != null) {
                            list.addAll(teamResponse.data);
                            Log.e("API_RESPONSE", "data: " + teamResponse.data);
                            Log.e("API_RESPONSE", "pagination: " + teamResponse.pagination);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isEnableToLoadMore = false;
                            } else {
                                isEnableToLoadMore = pagination.has_more;
                            }
                            if (oldPos - 1 >= 0)
                                adapter.notifyItemChanged(oldPos - 1);
                            adapter.notifyItemRangeInserted(oldPos, teamResponse.data.size());
                            binding.rcvLeagueToday.post(() -> loadingDialog.dismiss());
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FixtureResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            Log.e("call_api_data", "catch: ", e);
        }
    }

    public void fetchLiveMatch() {
        try {
            ApiDataService.apiService.callLiveMatch(ConstantApiData.KEY, "participants;scores;state;periods").enqueue(new Callback<LiveResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<LiveResponse> call, @NonNull Response<LiveResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        LiveResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            listLive.addAll(teamResponse.data);
                            Log.e("API_RESPONSE", "data: " + teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (FixtureLiveModel liveModel : listLive) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            liveMatchAdapter.notifyDataSetChanged();
                            binding.rcvLeagueToday.post(() -> {
                                binding.loadingLive.setVisibility(GONE);
                                binding.tvLiveNow.setText(getString(R.string.live_now) + " (" + listLive.size() + ")");
                            });
                        } else {
                            binding.loadingLive.setVisibility(GONE);
                        }
                    } else {
                        binding.loadingLive.setVisibility(GONE);
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LiveResponse> call, @NonNull Throwable t) {
                    binding.loadingLive.setVisibility(GONE);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            binding.loadingLive.setVisibility(GONE);
            Log.e("call_api_data", "catch: ", e);
        }
    }
}