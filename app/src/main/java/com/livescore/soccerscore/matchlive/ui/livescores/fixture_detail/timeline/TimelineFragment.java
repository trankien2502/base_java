package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentTimelineBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.EventDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;

public class TimelineFragment extends BaseFragment<FragmentTimelineBinding> {

    EventAdapter adapter;

    @Override
    public FragmentTimelineBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentTimelineBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        if (MatchDetailActivity.instance != null) {
            adapter = new EventAdapter(requireContext(), MatchDetailActivity.instance.fixtureDetailModel.events, MatchDetailActivity.instance.homeId);
            binding.rcvEvent.setAdapter(adapter);
        }
    }

    @Override
    public void bindView() {

    }
}