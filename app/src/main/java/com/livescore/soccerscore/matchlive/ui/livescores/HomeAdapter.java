package com.livescore.soccerscore.matchlive.ui.livescores;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.livescore.soccerscore.matchlive.ui.livescores.favourite.FavouriteFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.home.HomeFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.notification.NotificationFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.setting.SettingFragment;

public class HomeAdapter extends FragmentStateAdapter {
    public HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new FavouriteFragment();
            case 2: return new NotificationFragment();
            case 3: return new SettingFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
