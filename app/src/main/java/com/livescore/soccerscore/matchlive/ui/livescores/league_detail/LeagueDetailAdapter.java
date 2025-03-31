package com.livescore.soccerscore.matchlive.ui.livescores.league_detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_fixture.LeagueFixtureFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table.LeagueTableFragment;

public class LeagueDetailAdapter extends FragmentStateAdapter {
    public LeagueDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new LeagueFixtureFragment() : new LeagueTableFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
