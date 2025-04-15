package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentStatsFixtureBinding;
import com.livescore.soccerscore.matchlive.model.fixture.stats.StatsDetail;
import com.livescore.soccerscore.matchlive.model.fixture.stats.StatsHomeAndAway;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.FixtureDetailModel;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StatsFixtureFragment extends BaseFragment<FragmentStatsFixtureBinding> {

    long homeId = 0, awayId = 0;
    FixtureDetailModel fixtureDetailModel;
    Map<Long, StatsHomeAndAway> map = new HashMap<>();
    List<StatsHomeAndAway> list = new ArrayList<>();
    StatsAdapter adapter;

    @Override
    public FragmentStatsFixtureBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentStatsFixtureBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void initView() {
        adapter = new StatsAdapter(requireContext(), list);
        binding.rcvStats.setAdapter(adapter);
        if (MatchDetailActivity.instance != null) {
            homeId = MatchDetailActivity.instance.homeId;
            awayId = MatchDetailActivity.instance.awayId;
            if (MatchDetailActivity.instance.fixtureDetailModel != null) {
                fixtureDetailModel = MatchDetailActivity.instance.fixtureDetailModel;
                if (!fixtureDetailModel.statistics.isEmpty()) {
                    binding.noData.setVisibility(GONE);
                    for (StatsDetail statsDetail : fixtureDetailModel.statistics) {
                        StatsHomeAndAway stats = new StatsHomeAndAway();
                        if (map.containsKey(statsDetail.type_id)) {
                            stats = map.get(statsDetail.type_id);
                        }
                        assert stats != null;
                        stats.id = statsDetail.type_id;
                        stats.name = statsDetail.getType().name;
                        if (statsDetail.participant_id == homeId) {
                            stats.homeValue = statsDetail.getData().value;
                        } else {
                            stats.awayValue = statsDetail.getData().value;
                        }
                        map.put(statsDetail.type_id, stats);
                    }
                    for (StatsHomeAndAway value : map.values()) {
                        if (value.id != -1 && value.awayValue != -1 && value.homeValue != -1 && !Objects.equals(value.name, ""))
                            list.add(value);
                        Log.e("API_RESPONSE", "stats: " + value);
                    }
                    adapter.notifyDataSetChanged();
                    Log.e("API_RESPONSE", "stats: " + map.values().size());
                    Log.e("API_RESPONSE", "stats: " + list.size());

                } else {
                    binding.noData.setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    public void bindView() {

    }
}