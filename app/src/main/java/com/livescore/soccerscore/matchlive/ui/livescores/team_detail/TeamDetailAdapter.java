package com.livescore.soccerscore.matchlive.ui.livescores.team_detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture.TeamFixtureFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad.TeamSquadFragment;

public class TeamDetailAdapter extends FragmentStateAdapter {
    public TeamDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new TeamStatsFragment();
        else if (position == 1) {
            return new TeamFixtureFragment();
        } else return new TeamSquadFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
