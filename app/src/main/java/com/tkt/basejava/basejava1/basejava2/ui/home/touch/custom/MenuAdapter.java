package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MenuAdapter extends FragmentStateAdapter {
    public MenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new Menu1Fragment() : new Menu2Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
