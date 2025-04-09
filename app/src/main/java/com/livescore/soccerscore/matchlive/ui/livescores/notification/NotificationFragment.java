package com.livescore.soccerscore.matchlive.ui.livescores.notification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
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
import java.util.Collection;
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
                    } else {
                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureModel.id);
                        fixtureModel.cancelNotification(requireContext());
                    }
                    FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                    if (fixtureBase != null)
                        Toast.makeText(requireContext(), fixtureBase.name, Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(pos);

                } else {
                    FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                    FixtureModel fixture23 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);
                    if (fixtureBase == null) {
                        fixtureModel.isPin = true;
                        if (fixture23 != null) {
                            fixture23.isPin = true;
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixture23);
                        } else {
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                        }
                        adapter.notifyItemChanged(pos);
                        showPinnedMatch();
                        FixtureModel fixtureBase1 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                        if (fixtureBase1 != null) {
                            Toast.makeText(requireContext(), fixtureBase1.name, Toast.LENGTH_SHORT).show();
                            fixtureBase1.schedule(requireContext());
                        }

                    } else {
                        if (!fixtureBase.isPin) {
                            fixtureModel.isPin = true;
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                            adapter.notifyItemChanged(pos);
                            showPinnedMatch();
                            FixtureModel fixtureBase1 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                            if (fixtureBase1 != null) {
                                Toast.makeText(requireContext(), fixtureBase1.name, Toast.LENGTH_SHORT).show();
                                fixtureBase1.schedule(requireContext());
                            }
                        } else {
                            MatchPinWarnDialog dialog = new MatchPinWarnDialog(requireContext(), false);
                            dialog.binding.btnCancel.setOnClickListener(v -> {
                                dialog.dismiss();
                            });
                            dialog.binding.btnReplace.setOnClickListener(v -> {
                                fixtureModel.isPin = true;
                                adapter.notifyItemChanged(pos);
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                                dialog.dismiss();
                                FixtureModel fixtureBase1 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                                if (fixtureBase1 != null) {
                                    Toast.makeText(requireContext(), fixtureBase1.name, Toast.LENGTH_SHORT).show();
                                    fixtureBase1.schedule(requireContext());
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).id != fixtureModel.id && list.get(i).isPin) {
                                        list.get(i).isPin = false;
                                        list.get(i).cancelNotification(requireContext());
                                        adapter.notifyItemChanged(i);
                                        break;
                                    }
                                }
                            });
                            dialog.show();
                        }
                    }

                }


            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {
                FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);
                NotificationDialog dialog = new NotificationDialog(requireContext(), false);

                dialog.initCallBack(new DialogNotificationCallBack() {
                    @Override
                    public void cancel() {
                        dialog.dismiss();
                    }

                    @Override
                    public void save(FixtureModel fixtureModel1) {
                        if (fixtureModel1.isAlarm) {
                            if (fixtureBase != null) {
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                            } else
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel1);
                            FixtureModel fixtureBase1 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel1.id);
                            if (fixtureBase1 != null) {
                                Toast.makeText(requireContext(), fixtureBase1.name, Toast.LENGTH_SHORT).show();
                                fixtureBase1.schedule(requireContext());
                            }
                        } else {
                            if (fixtureBase != null) {
                                if (fixtureBase.isPin) {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                    fixtureModel1.schedule(requireContext());
                                } else {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureBase.id);
                                    fixtureBase.cancelNotification(requireContext());
                                }

                            }
                        }
                        adapter.notifyItemChanged(pos);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.initFixture(fixtureModel);
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