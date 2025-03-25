package com.livescore.soccerscore.matchlive.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.dialog.GoToSettingDialog;
import com.livescore.soccerscore.matchlive.dialog.exit.ExitAppDialog;
import com.livescore.soccerscore.matchlive.dialog.exit.IClickDialogExit;
import com.livescore.soccerscore.matchlive.dialog.rate.IClickDialogRate;
import com.livescore.soccerscore.matchlive.dialog.rate.RatingDialog;
import com.livescore.soccerscore.matchlive.ui.home.favourite.FavouriteFragment;
import com.livescore.soccerscore.matchlive.ui.home.live.HomeFragment;
import com.livescore.soccerscore.matchlive.ui.home.notification.NotificationFragment;
import com.livescore.soccerscore.matchlive.ui.home.setting.SettingFragment;
import com.livescore.soccerscore.matchlive.ui.setting.SettingActivity;
import com.livescore.soccerscore.matchlive.util.EventTracking;
import com.livescore.soccerscore.matchlive.util.PermissionManager;
import com.livescore.soccerscore.matchlive.util.SPUtils;
import com.livescore.soccerscore.matchlive.util.SharePrefUtils;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.livescore.soccerscore.matchlive.util.SystemUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private static final int STATE_HOME = 1;
    private static final int STATE_FAVOURITE = 2;
    private static final int STATE_NOTIFICATION = 3;
    private static final int STATE_SETTING = 4;
    private int state = 1;

    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));


    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "home_view");
        changeState();
    }

    @Override
    public void bindView() {
        binding.llHome.setOnClickListener(view -> {
            state = STATE_HOME;
            changeState();
        });
        binding.llNotification.setOnClickListener(view -> {
//            state = STATE_NOTIFICATION;
//            changeState();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog dialog = new DatePickerDialog(this);
                dialog.show();
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(getBaseContext(), "Ngày đã chọn: " + year + month + dayOfMonth, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.llFavourite.setOnClickListener(view -> {
            state = STATE_FAVOURITE;
            changeState();

        });
        binding.llSetting.setOnClickListener(view -> {
//            state = STATE_SETTING;
//            changeState();
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = sdf.format(new Date(selection));
                Toast.makeText(this, "Ngày đã chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
            });
        });

    }

    @Override
    public void onBack() {
        exitApp();
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
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
                binding.ivHome.setImageResource(R.drawable.live_s);
                binding.tvHome.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_FAVOURITE:
                replaceFragment(new FavouriteFragment());
                binding.ivFavourite.setImageResource(R.drawable.star_s);
                binding.tvFavourite.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_NOTIFICATION:
                replaceFragment(new NotificationFragment());
                binding.ivNotification.setImageResource(R.drawable.clock_s);
                binding.tvNotification.setTextColor(Color.parseColor("#0094FD"));
                break;
            case STATE_SETTING:
                replaceFragment(new SettingFragment());
                binding.ivSetting.setImageResource(R.drawable.setting_s);
                binding.tvSetting.setTextColor(Color.parseColor("#0094FD"));
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
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
