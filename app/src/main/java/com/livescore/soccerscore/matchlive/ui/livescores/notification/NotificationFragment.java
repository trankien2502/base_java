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

            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {

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
}