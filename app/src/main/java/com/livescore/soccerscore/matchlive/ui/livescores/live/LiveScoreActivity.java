package com.livescore.soccerscore.matchlive.ui.livescores.live;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.databinding.ActivityLiveScoreBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.model.live.FixtureLiveModel;
import com.livescore.soccerscore.matchlive.model.live.LiveResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveScoreActivity extends BaseActivity<ActivityLiveScoreBinding> {


    LoadingDialog loadingDialog;
    List<FixtureLiveModel> listLive = new ArrayList<>();
    LiveMatchActivityAdapter liveMatchAdapter;

    @Override
    public ActivityLiveScoreBinding getBinding() {
        return ActivityLiveScoreBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        liveMatchAdapter = new LiveMatchActivityAdapter(this, listLive, new LiveMatchClickCallBack() {
            @Override
            public void detail(FixtureLiveModel fixtureModel) {
                Toast.makeText(getBaseContext(), "select " + fixtureModel.id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                resultLauncher.launch(intent);
            }
        });
        binding.rcvLive.setAdapter(liveMatchAdapter);
        if (IsNetWork.haveNetworkConnection(this)) {
            listLive.clear();
            loadingDialog = new LoadingDialog(this, false);
            loadingDialog.show();
            fetchLiveMatch();
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(v -> {
            if (IsNetWork.haveNetworkConnection(this)) {
                listLive.clear();
                liveMatchAdapter.notifyDataSetChanged();
                loadingDialog = new LoadingDialog(this, false);
                loadingDialog.show();
                fetchLiveMatch();
            } else {
                Log.e("call_api_data", "No internet to call api");
            }
        });
        binding.ivBack.setOnClickListener(v -> onBack());
    }

    public void fetchLiveMatch() {
        try {
            ApiDataService.apiService.callLiveMatch(ConstantApiData.KEY,ConstantApiData.TIMEZONE, "participants;scores;state;periods").enqueue(new Callback<LiveResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<LiveResponse> call, @NonNull Response<LiveResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        LiveResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
//                            listLive.addAll(teamResponse.data);
                            for (FixtureLiveModel fixtureLiveModel: teamResponse.data){
                                if (!fixtureLiveModel.getState().short_name.equals("NS")&&!fixtureLiveModel.getState().short_name.equals("FT")) listLive.add(fixtureLiveModel);
                            }
                            Log.e("API_RESPONSE", "data: " + teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (FixtureLiveModel liveModel : listLive) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            liveMatchAdapter.notifyDataSetChanged();
                            binding.rcvLive.post(() -> {
                                loadingDialog.dismiss();
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
                public void onFailure(@NonNull Call<LiveResponse> call, @NonNull Throwable t) {
                    loadingDialog.dismiss();
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            Log.e("call_api_data", "catch: ", e);
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}