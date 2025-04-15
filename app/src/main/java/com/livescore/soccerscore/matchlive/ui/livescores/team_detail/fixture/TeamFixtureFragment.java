package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
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
import com.livescore.soccerscore.matchlive.database.fixture.FixtureDatabase;
import com.livescore.soccerscore.matchlive.dialog.MatchPinWarnDialog;
import com.livescore.soccerscore.matchlive.dialog.MatchPinnedDialog;
import com.livescore.soccerscore.matchlive.dialog.notification.DialogNotificationCallBack;
import com.livescore.soccerscore.matchlive.dialog.notification.NotificationDialog;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTeamFixtureBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.model.fixture.TeamFixtureResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.util.GoToSettingCallBack;
import com.livescore.soccerscore.matchlive.util.PermissionManager;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        initAdapter();
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


    @SuppressLint("NotifyDataSetChanged")
    public void fetchFixtureTeam(long teamId) {
        List<FixtureModel> fixtureModelList = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getAllFixture();
        try {
            ApiDataService.apiService.callTeamFixture(teamId, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "upcoming.participants;upcoming.scores;upcoming.state").enqueue(new Callback<TeamFixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamFixtureResponse> call, @NonNull Response<TeamFixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON schedule: " + new Gson().toJson(response.body()));
                        TeamFixtureResponse teamFixtureResponse = response.body();
                        Log.e("API_RESPONSE", "data: " + teamFixtureResponse.getData());
                        if (teamFixtureResponse.data != null) {

                            List<FixtureModel> todayList = teamFixtureResponse.getData().upcoming;
                            for (int i = 0; i < todayList.size(); i++) {
                                FixtureModel fixtureModel = todayList.get(i);
                                Iterator<FixtureModel> iterator = fixtureModelList.iterator();
                                while (iterator.hasNext()) {
                                    FixtureModel fixtureModelDB = iterator.next();
                                    if (fixtureModel.id == fixtureModelDB.id) {
                                        todayList.set(i, fixtureModelDB);
                                        iterator.remove();
                                        break;
                                    }
                                }
                            }
                            list.addAll(todayList);
                        }
                        for (FixtureModel fixtureModel : list) {
                            Log.e("API_RESPONSE", "data: " + fixtureModel);
                        }
                        loadingDialog.dismiss();
                        fixtureAdapter.notifyDataSetChanged();
                        if (list.isEmpty())
                            binding.noData.setVisibility(VISIBLE);
                        else {
                            binding.noData.setVisibility(GONE);
                        }
                    } else {
                        loadingDialog.dismiss();
                        if (list.isEmpty())
                            binding.noData.setVisibility(VISIBLE);
                        else {
                            binding.noData.setVisibility(GONE);
                        }
                        fixtureAdapter.notifyDataSetChanged();
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
                    if (list.isEmpty())
                        binding.noData.setVisibility(VISIBLE);
                    else {
                        binding.noData.setVisibility(GONE);
                    }
                    fixtureAdapter.notifyDataSetChanged();
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            loadingDialog.dismiss();
            if (list.isEmpty())
                binding.noData.setVisibility(VISIBLE);
            else {
                binding.noData.setVisibility(GONE);
            }
            fixtureAdapter.notifyDataSetChanged();
            Log.e("call_api_data", "catch: ", e);
        }
    }

    private void initAdapter() {
        fixtureAdapter = new FixtureAdapter(requireContext(), list, new FixtureClickCallBack() {
            @Override
            public void select(int pos, FixtureModel fixtureModel) {
                Log.e("check_id", "select " + fixtureModel.id);
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
            }

            @Override
            public void pin(int pos, FixtureModel fixtureModel) {
                if (PermissionManager.checkOverlayPermission(requireContext())) {
                    if (fixtureModel.isPin) {
                        fixtureModel.isPin = false;
                        if (fixtureModel.isAlarm) {
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                            fixtureModel.schedule(requireContext());
                            fixtureAdapter.notifyItemChanged(pos);
                        } else {
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureModel.id);
                            fixtureModel.cancelNotification(requireContext());
                            list.remove(pos);
                            fixtureAdapter.notifyItemRemoved(pos);
                        }
                    } else {
                        FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                        FixtureModel fixture23 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);

                        if (fixtureBase == null) {
                            fixtureModel.isPin = true;
                            if (fixture23 != null) {
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                            } else {
                                fixtureModel.create_at = System.currentTimeMillis();
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                            }
                            fixtureModel.schedule(requireContext());
                            fixtureAdapter.notifyItemChanged(pos);
                            showPinnedMatch();
                        } else {
                            MatchPinWarnDialog dialog = new MatchPinWarnDialog(requireContext(), false);
                            dialog.binding.btnCancel.setOnClickListener(v -> {
                                dialog.dismiss();
                            });
                            if (fixture23 != null) {
                                dialog.binding.btnReplace.setOnClickListener(v -> {
                                    fixtureModel.isPin = true;
                                    fixtureModel.schedule(requireContext());
                                    fixtureAdapter.notifyItemChanged(pos);
                                    dialog.dismiss();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).id != fixtureModel.id && list.get(i).isPin) {
                                            list.get(i).isPin = false;
                                            fixtureAdapter.notifyItemChanged(i);
                                            break;
                                        }
                                    }
                                    if (fixtureBase.isAlarm) {
                                        fixtureBase.isPin = false;
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureBase);
                                        fixtureBase.schedule(requireContext());
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().deletePin();
                                        fixtureBase.cancelNotification(requireContext());
                                    }
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                                });
                            } else {
                                dialog.binding.btnReplace.setOnClickListener(v -> {
                                    fixtureModel.isPin = true;
                                    fixtureModel.create_at = System.currentTimeMillis();
                                    fixtureModel.schedule(requireContext());
                                    fixtureAdapter.notifyItemChanged(pos);
                                    dialog.dismiss();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).id != fixtureModel.id && list.get(i).isPin) {
                                            list.get(i).isPin = false;
                                            fixtureAdapter.notifyItemChanged(i);
                                            break;
                                        }
                                    }
                                    FixtureModel pinNow = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                                    if (pinNow.isAlarm) {
                                        pinNow.isPin = false;
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(pinNow);
                                        pinNow.schedule(requireContext());
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().deletePin();
                                        pinNow.cancelNotification(requireContext());
                                    }
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                                });
                            }
                            dialog.show();
                        }

                    }
                } else {
                    SPUtils.showDialogGotoSetting(requireContext(), 2, new GoToSettingCallBack() {
                        @Override
                        public void goToSetting(Intent intent) {
                            startArc(intent);
                        }
                    });
                }
                if (list.isEmpty())
                    binding.noData.setVisibility(VISIBLE);
                else {
                    binding.noData.setVisibility(GONE);
                }

            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {
                if (PermissionManager.checkNotificationPermission(requireContext())) {
                    FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);
                    NotificationDialog dialog = new NotificationDialog(requireContext(), false);
                    dialog.initFixture(fixtureModel);
                    dialog.initCallBack(new DialogNotificationCallBack() {
                        @Override
                        public void cancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void save(FixtureModel fixtureModel1) {
                            Log.d("alarmcheck", "save: " + fixtureModel1);
                            if (fixtureModel1.isAlarm) {
                                if (fixtureBase != null) {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                } else
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel1);
                                fixtureModel1.schedule(requireContext());
                                list.set(pos, fixtureModel1);
                                fixtureAdapter.notifyItemChanged(pos);
                            } else {
                                if (fixtureBase != null) {
                                    if (fixtureBase.isPin) {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                        Log.d("alarmcheck", "schedule: " + fixtureModel1);
                                        fixtureModel1.schedule(requireContext());
                                        list.set(pos, fixtureModel1);
                                        fixtureAdapter.notifyItemChanged(pos);
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureBase.id);
                                        fixtureBase.cancelNotification(requireContext());
                                        list.remove(pos);
                                        fixtureAdapter.notifyItemRemoved(pos);
                                    }

                                }
                            }
                            if (list.isEmpty())
                                binding.noData.setVisibility(VISIBLE);
                            else {
                                binding.noData.setVisibility(GONE);
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    SPUtils.showDialogGotoSetting(requireContext(), 1, new GoToSettingCallBack() {
                        @Override
                        public void goToSetting(Intent intent) {
                            startArc(intent);
                        }
                    });
                }
            }
        });
        binding.rcvFixture.setAdapter(fixtureAdapter);
    }

    public void showPinnedMatch() {
        MatchPinnedDialog dialog = new MatchPinnedDialog(requireContext(), false);
        dialog.binding.btnOK.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}