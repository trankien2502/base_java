package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.base.BaseFragment;
import com.tkt.basejava.basejava1.basejava2.databinding.PopupSelectAction2Binding;
import com.tkt.basejava.basejava1.basejava2.databinding.PopupSelectActionBinding;

public class Menu2Fragment extends BaseFragment<PopupSelectAction2Binding> {

    public static Menu2Fragment instance;
    @Override
    public PopupSelectAction2Binding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return PopupSelectAction2Binding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        instance = this;
    }

    @Override
    public void bindView() {

    }
    private void startArc(Intent intent) {
        if (getContext() instanceof CustomMenuActivity) {
            CustomMenuActivity customMenuActivity = (CustomMenuActivity) getContext();
            customMenuActivity.resultLauncher.launch(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}