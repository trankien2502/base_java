package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup.LineupFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats.StatsFixtureFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.table.TableFixtureFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline.TimelineFragment;

public class MatchDetailAdapter extends FragmentStateAdapter {
    public MatchDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new StatsFixtureFragment();
            case 2:
                return new LineupFragment();
            case 3:
                return new TableFixtureFragment();
            default:
                return new TimelineFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
