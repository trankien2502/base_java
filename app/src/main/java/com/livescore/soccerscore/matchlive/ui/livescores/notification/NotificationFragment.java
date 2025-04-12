package com.livescore.soccerscore.matchlive.ui.livescores.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.database.fixture.FixtureDatabase;
import com.livescore.soccerscore.matchlive.databinding.FragmentNotificationBinding;
import com.livescore.soccerscore.matchlive.dialog.MatchPinWarnDialog;
import com.livescore.soccerscore.matchlive.dialog.MatchPinnedDialog;
import com.livescore.soccerscore.matchlive.dialog.notification.DialogNotificationCallBack;
import com.livescore.soccerscore.matchlive.dialog.notification.NotificationDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.home.FixtureClickCallBack;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding> {

    FixtureAdapter adapter;
    List<FixtureModel> list = new ArrayList<>();

    @Override
    public FragmentNotificationBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentNotificationBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        list = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getAllFixture();
        adapter = new FixtureAdapter(requireContext(), list, new FixtureClickCallBack() {
            @Override
            public void select(int pos, FixtureModel fixtureModel) {
                Toast.makeText(requireContext(), "select " + fixtureModel.id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
            }

            @Override
            public void pin(int pos, FixtureModel fixtureModel) {
                if (fixtureModel.isPin) {
                    fixtureModel.isPin = false;
                    if (fixtureModel.isAlarm) {
                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                        fixtureModel.schedule(requireContext());
                        adapter.notifyItemChanged(pos);
                    } else {
                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureModel.id);
                        fixtureModel.cancelNotification(requireContext());
                        list.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                } else {
                    FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                    FixtureModel fixture23 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);

                    if (fixtureBase == null) {
                        if (fixture23 != null) {
                            fixture23.isPin = true;
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixture23);
                            fixture23.schedule(requireContext());
                            list.set(pos, fixture23);
                        } else {
                            fixtureModel.isPin = true;
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                            fixtureModel.schedule(requireContext());
                            list.set(pos, fixtureModel);
                        }
                        adapter.notifyItemChanged(pos);
                        showPinnedMatch();
                    } else {
                        if (fixture23 != null) {
                            if (fixtureBase.id != fixture23.id) {
                                MatchPinWarnDialog dialog = new MatchPinWarnDialog(requireContext(), false);
                                dialog.binding.btnCancel.setOnClickListener(v -> {
                                    dialog.dismiss();
                                });
                                dialog.binding.btnReplace.setOnClickListener(v -> {
                                    fixtureModel.isPin = true;
                                    adapter.notifyItemChanged(pos);
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                                    fixtureModel.schedule(requireContext());
                                    dialog.dismiss();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).id != fixtureModel.id && list.get(i).isPin) {
                                            list.get(i).isPin = false;
                                            if (list.get(i).isAlarm) {
                                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(list.get(i));
                                                list.get(i).schedule(requireContext());
                                                adapter.notifyItemChanged(i);
                                            } else {
                                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(list.get(i).id);
                                                list.get(i).cancelNotification(requireContext());
                                                adapter.notifyItemChanged(i);
                                            }
                                            break;
                                        }
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }

                }


            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {
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
                            adapter.notifyItemChanged(pos);
                        } else {
                            if (fixtureBase != null) {
                                if (fixtureBase.isPin) {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                    Log.d("alarmcheck", "schedule: " + fixtureModel1);
                                    fixtureModel1.schedule(requireContext());
                                    list.set(pos, fixtureModel1);
                                    adapter.notifyItemChanged(pos);
                                } else {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureBase.id);
                                    fixtureBase.cancelNotification(requireContext());
                                    list.remove(pos);
                                    adapter.notifyItemRemoved(pos);
                                }

                            }
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        binding.rcvNotification.setAdapter(adapter);
    }

    @Override
    public void bindView() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
//        if (adapter != null) {
//            list.clear();
//            list = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getAllFixture();
//            adapter.notifyDataSetChanged();
//        }
    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }

    public void showPinnedMatch() {
        MatchPinnedDialog dialog = new MatchPinnedDialog(requireContext(), false);
        dialog.binding.btnOK.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}