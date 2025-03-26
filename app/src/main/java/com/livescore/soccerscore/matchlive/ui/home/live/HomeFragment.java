package com.livescore.soccerscore.matchlive.ui.home.live;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.CallApiUtils;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentHomeBinding;
import com.livescore.soccerscore.matchlive.ui.home.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.language.adapter.LanguageStartAdapter;
import com.livescore.soccerscore.matchlive.ui.language.model.LanguageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import travel.ithaka.android.horizontalpickerlib.PickerLayoutManager;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    public FragmentHomeBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.datePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@Nullable Date date) {
                if (date != null) {
                    // do something with selected date
                    Toast.makeText(requireContext(), "date: " + date, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            CallApiUtils.fetchFixtureDatePage("2006-03-25",1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
    }

    @Override
    public void bindView() {

    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }
}