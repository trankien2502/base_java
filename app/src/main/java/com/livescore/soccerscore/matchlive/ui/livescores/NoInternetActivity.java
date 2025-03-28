package com.livescore.soccerscore.matchlive.ui.livescores;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.databinding.ActivityNoInternetBinding;
import com.livescore.soccerscore.matchlive.service.NetworkReceiver;
import com.livescore.soccerscore.matchlive.util.SystemUtil;

import java.util.Objects;

public class NoInternetActivity extends AppCompatActivity {
    ActivityNoInternetBinding binding;
    public static NoInternetActivity instance = null;
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        SystemUtil.setLocale(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //make fully Android Transparent Status bar
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Log.e("check_no_internet","count me");
        // Thiết lập màu trong suốt cho thanh điều hướng
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        binding = ActivityNoInternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        });
//        binding.getRoot().setPadding(
//                binding.getRoot().getPaddingLeft(),
//                binding.getRoot().getPaddingTop() + getStatusBarHeight(),
//                binding.getRoot().getPaddingRight(),
//                binding.getRoot().getPaddingBottom()
//        );
        hideNavigation();
        initView();
        bindView();

    }

    public void hideFullNavigation() {
        try {
            int flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().getDecorView().setSystemUiVisibility(flags);

            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void startNextActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (ConstantRemote.open_resume && CheckAds.getInstance().isShowAds(this)) {
//            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
//        } else {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
//        }
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        // Đăng ký với cờ phù hợp
        ContextCompat.registerReceiver(
                this,
                networkReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED // Đảm bảo receiver không được export
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    public void finishThisActivity() {
        finish();
        overridePendingTransition(R.anim.in_left, R.anim.out_right);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void hideNavigation() {
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
        }

        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0) {
                new Handler().postDelayed(() -> {
                    WindowInsetsControllerCompat windowInsetsController1;
                    if (Build.VERSION.SDK_INT >= 30) {
                        windowInsetsController1 = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    } else {
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), binding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("check_no_internet","destroy");
        instance = null;
    }

    private void initView() {

    }

    private void bindView() {
        binding.tvTryAgain.setOnClickListener(v -> {
            Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intentWifi);
        });
    }

    private void onBack() {

    }
}
