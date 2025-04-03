package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.databinding.ActivityMatchDeatilBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.goal.GoalAwayAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.goal.GoalHomeAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline.OddDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.live.PeriodModel;
import com.livescore.soccerscore.matchlive.util.EventTracking;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailActivity extends BaseActivity<ActivityMatchDeatilBinding> {

    public LoadingDialog loadingDialog;
    public FixtureDetailModel fixtureDetailModel;
    public long homeId = 0, awayId = 0;
    MatchDetailAdapter adapter;
    public static MatchDetailActivity instance;

    @Override
    public ActivityMatchDeatilBinding getBinding() {
        return ActivityMatchDeatilBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void initView() {
        adapter = new MatchDetailAdapter(this);
        EventTracking.logEvent(this, "home_view");
        binding.main.setPadding(
                binding.main.getPaddingLeft(),
                binding.main.getPaddingTop() + getStatusBarHeight(),
                binding.main.getPaddingRight(),
                binding.main.getPaddingBottom()
        );
        long fixtureId = getIntent().getLongExtra(SPUtils.INTENT_FIXTURE, 0);
        loadingDialog = new LoadingDialog(this, false);
        if (IsNetWork.haveNetworkConnection(this)) {
            loadingDialog.show();
            fetchFixtureDetail(fixtureId);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> onBack());
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeState(position);
            }
        });
        binding.tvTimeLine.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
        });
        binding.tvStats.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
        });
        binding.tvLineup.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(2);
        });
        binding.tvTable.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(3);
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    public void fetchFixtureDetail(long id) {
        try {
            ApiDataService.apiService.callFixtureDetail(id, ConstantApiData.KEY, "participants;periods;league.country;venue;state;scores;events.type;events.period;lineups.position;odds", "markets:1;bookmakers:2")
                    .enqueue(new Callback<FixtureDetailResponse>() {
                        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                        @Override
                        public void onResponse(@NonNull Call<FixtureDetailResponse> call, @NonNull Response<FixtureDetailResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                                FixtureDetailResponse teamResponse = response.body();
                                if (teamResponse.data != null) {
                                    fixtureDetailModel = teamResponse.getData();
                                    Log.e("API_RESPONSE", "data: " + fixtureDetailModel);
                                    Log.e("API_RESPONSE", "state: " + fixtureDetailModel.state);
                                    Log.e("call_api_data", "call true:");
                                    if (!fixtureDetailModel.scores.isEmpty()) {
                                        for (ScoreModel score : fixtureDetailModel.scores)
                                            Log.e("API_RESPONSE", "score: " + score);
                                    }

                                    if (!fixtureDetailModel.periods.isEmpty()) {
                                        for (PeriodModel periodModel : fixtureDetailModel.periods)
                                            Log.e("API_RESPONSE", "periods: " + periodModel);
                                    }
                                    if (!fixtureDetailModel.participants.isEmpty()) {
                                        for (TeamInMatch team : fixtureDetailModel.participants)
                                            Log.e("API_RESPONSE", "participants: " + team);
                                    }
                                    if (!fixtureDetailModel.lineups.isEmpty()) {
                                        for (LineupDetail lineupDetail : fixtureDetailModel.lineups)
                                            Log.e("API_RESPONSE", "lineups: " + lineupDetail);
                                    }
                                    if (!fixtureDetailModel.events.isEmpty()) {
                                        for (EventDetail eventDetail : fixtureDetailModel.events)
                                            Log.e("API_RESPONSE", "events: " + eventDetail);
                                    }
                                    if (!fixtureDetailModel.odds.isEmpty()) {
                                        for (OddDetail oddDetail : fixtureDetailModel.odds)
                                            Log.e("API_RESPONSE", "odds: " + oddDetail);
                                    }
//                            liveMatchAdapter.notifyDataSetChanged();
//                            binding.rcvLive.post(() -> {
//                                loadingDialog.dismiss();
//                            });
                                    loadingDialog.dismiss();
                                    showView();
                                } else {
                                    loadingDialog.dismiss();
                                }
                            } else {
                                loadingDialog.dismiss();
                                Log.e("call_api_data", "call false: Code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<FixtureDetailResponse> call, @NonNull Throwable t) {
                            loadingDialog.dismiss();
                            Log.e("call_api_data", "onfailure" + t);
                        }
                    });

        } catch (Exception e) {
            loadingDialog.dismiss();
            Log.e("call_api_data", "catch: ", e);
        }
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private void showView() {

        if (fixtureDetailModel.participants.get(0).getMeta().location.equals("home")) {
            homeId = fixtureDetailModel.participants.get(0).getId();
            awayId = fixtureDetailModel.participants.get(1).getId();
            Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivHome);
            Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivAway);
            Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivHome1);
            Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivAway1);
            binding.tvHome.setText(fixtureDetailModel.participants.get(0).getName());
            binding.tvAway.setText(fixtureDetailModel.participants.get(1).getName());
        } else {
            homeId = fixtureDetailModel.participants.get(1).getId();
            awayId = fixtureDetailModel.participants.get(0).getId();
            Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivHome);
            Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivAway);
            Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(binding.ivHome1);
            Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(binding.ivAway1);
            binding.tvHome.setText(fixtureDetailModel.participants.get(1).getName());
            binding.tvAway.setText(fixtureDetailModel.participants.get(0).getName());
        }
        binding.tvHome.setSelected(true);
        binding.tvAway.setSelected(true);
        binding.tvStatus.setText("(" + fixtureDetailModel.getState().short_name + ")");
        if (fixtureDetailModel.periods.isEmpty()) {
            binding.tvTime.setText("");
        } else
            binding.tvTime.setText(fixtureDetailModel.periods.get(fixtureDetailModel.periods.size() - 1).minutes + "'");
        if (!fixtureDetailModel.scores.isEmpty()) {
            int scoreHome = 0, scoreAway = 0;
            for (ScoreModel scoreModel : fixtureDetailModel.scores) {
                if (scoreModel.description.equals("CURRENT")) {
                    if (scoreModel.getScore().participant.equals("home")) {
                        scoreHome = scoreModel.getScore().goals;
                    }
                    if (scoreModel.getScore().participant.equals("away")) {
                        scoreAway = scoreModel.getScore().goals;
                    }
                }
            }
            binding.tvScore.setText(scoreHome + " - " + scoreAway);
            binding.tvScore1.setText(scoreHome + " - " + scoreAway);
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(fixtureDetailModel.starting_at);
            assert date != null;
            String timeText = timeFormat.format(date);
            String dateText = dateFormat.format(date);
            binding.tvDate.setText(dateText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!fixtureDetailModel.events.isEmpty()) {
            List<EventDetail> listHome = new ArrayList<>();
            List<EventDetail> listAway = new ArrayList<>();
            for (EventDetail eventDetail : fixtureDetailModel.events) {
                if (eventDetail.participant_id == homeId) {
                    if (eventDetail.getType().developer_name.equals("PENALTY") || eventDetail.getType().developer_name.equals("GOAL") || eventDetail.getType().developer_name.equals("OWNGOAL")) {
                        listHome.add(eventDetail);
                    }
                } else if (eventDetail.participant_id == awayId) {
                    if (eventDetail.getType().developer_name.equals("PENALTY") || eventDetail.getType().developer_name.equals("GOAL") || eventDetail.getType().developer_name.equals("OWNGOAL")) {
                        listAway.add(eventDetail);
                    }
                }
            }
            GoalAwayAdapter goalAwayAdapter = new GoalAwayAdapter(this, awayId, listAway);
            binding.rcvBallAway.setAdapter(goalAwayAdapter);
            GoalHomeAdapter goalHomeAdapter = new GoalHomeAdapter(this, homeId, listHome);
            binding.rcvBallHome.setAdapter(goalHomeAdapter);
        }
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setCurrentItem(0);
    }

    private void resetChange() {
        binding.tvTimeLine.setBackgroundResource(0);
        binding.tvStats.setBackgroundResource(0);
        binding.tvLineup.setBackgroundResource(0);
        binding.tvTable.setBackgroundResource(0);
        binding.tvTimeLine.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvStats.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvLineup.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvTable.setTextColor(Color.parseColor("#a3a3a3"));
    }

    private void changeState(int state) {
        resetChange();
        if (state == 1) {
            binding.tvStats.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvStats.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 0) {
            binding.tvTimeLine.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTimeLine.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 2) {
            binding.tvLineup.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLineup.setTextColor(Color.parseColor("#0094FD"));
        } else if (state == 3) {
            binding.tvTable.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTable.setTextColor(Color.parseColor("#0094FD"));
        }
    }
}