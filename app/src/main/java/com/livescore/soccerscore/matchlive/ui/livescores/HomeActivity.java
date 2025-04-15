package com.livescore.soccerscore.matchlive.ui.livescores;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.dialog.exit.ExitAppDialog;
import com.livescore.soccerscore.matchlive.dialog.exit.IClickDialogExit;
import com.livescore.soccerscore.matchlive.ui.livescores.favourite.FavouriteFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.home.HomeFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.notification.NotificationFragment;
import com.livescore.soccerscore.matchlive.ui.livescores.setting.SettingFragment;
import com.livescore.soccerscore.matchlive.util.EventTracking;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.databinding.ActivityHomeBinding;


import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private static final int STATE_HOME = 1;
    private static final int STATE_FAVOURITE = 2;
    private static final int STATE_NOTIFICATION = 3;
    private static final int STATE_SETTING = 4;
    private int state = 1;
    HomeAdapter adapter;

    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));


    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        adapter = new HomeAdapter(this);
        EventTracking.logEvent(this, "home_view");
//        binding.frContentHome.setAdapter(adapter);
//        binding.frContentHome.setUserInputEnabled(false);
        changeState();
    }

    @Override
    public void bindView() {
        binding.llHome.setOnClickListener(view -> {
            state = STATE_HOME;
            changeState();
        });
        binding.llNotification.setOnClickListener(view -> {
            state = STATE_NOTIFICATION;
            changeState();

        });
        binding.llFavourite.setOnClickListener(view -> {
            state = STATE_FAVOURITE;
            changeState();

        });
        binding.llSetting.setOnClickListener(view -> {
            state = STATE_SETTING;
            changeState();

        });

    }

    @Override
    public void onBack() {
        exitApp();
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
//            if (state == STATE_FAVOURITE)
//            changeState();
            Log.d("activity_check", "home");
        }
    });

    private void resetChange() {
        binding.ivHome.setImageResource(R.drawable.live_sn);
        binding.ivFavourite.setImageResource(R.drawable.star_sn);
        binding.ivNotification.setImageResource(R.drawable.clock);
        binding.ivSetting.setImageResource(R.drawable.setting_sn);
        binding.tvHome.setTextColor(Color.parseColor("#809EB4"));
        binding.tvFavourite.setTextColor(Color.parseColor("#809EB4"));
        binding.tvSetting.setTextColor(Color.parseColor("#809EB4"));
        binding.tvNotification.setTextColor(Color.parseColor("#809EB4"));
    }

    private void changeState() {
        resetChange();
        switch (state) {
            case STATE_HOME:
                replaceFragment(new HomeFragment());
//                binding.frContentHome.setCurrentItem(0);
                binding.ivHome.setImageResource(R.drawable.live_s);
                binding.tvHome.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_FAVOURITE:
//                binding.frContentHome.setCurrentItem(1);
                replaceFragment(new FavouriteFragment());
                binding.ivFavourite.setImageResource(R.drawable.star_s);
                binding.tvFavourite.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_NOTIFICATION:
//                binding.frContentHome.setCurrentItem(2);
//                adapter.notifyItemChanged(2);
                replaceFragment(new NotificationFragment());
                binding.ivNotification.setImageResource(R.drawable.clock_s);
                binding.tvNotification.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_SETTING:
//                binding.frContentHome.setCurrentItem(3);
                replaceFragment(new SettingFragment());
                binding.ivSetting.setImageResource(R.drawable.setting_s);
                binding.tvSetting.setTextColor(Color.parseColor("#0094FD"));
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frContentHome, fragment)
                .commit();
    }

    private void exitApp() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this, true);
        exitAppDialog.init(new IClickDialogExit() {
            @Override
            public void cancel() {
                exitAppDialog.dismiss();
            }

            @Override
            public void quit() {
                exitAppDialog.dismiss();
                finishAffinity();
            }
        });

        try {
            exitAppDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
