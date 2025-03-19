package com.assistivetouch.easytouch.homebutton.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewbinding.ViewBinding;

import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ui.home.AllAppActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.ScreenRecorderActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.ScreenshotActivity;
import com.assistivetouch.easytouch.homebutton.ui.splash.SplashActivity;
import com.assistivetouch.easytouch.homebutton.ui.welcome.WelcomeBackActivity;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
import com.assistivetouch.easytouch.homebutton.ui.intro.IntroActivity;
import com.facebook.all.All;

import java.util.Objects;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    public VB binding;

    public abstract VB getBinding();

    public abstract void initView();

    public abstract void bindView();

    public abstract void onBack();

    Animation animation;
    public boolean isResume = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //make fully Android Transparent Status bar
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

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
        binding = getBinding();
        setContentView(binding.getRoot());

        animation = AnimationUtils.loadAnimation(this, R.anim.onclick);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        });
        if (!(this instanceof IntroActivity)) {
            binding.getRoot().setPadding(
                    binding.getRoot().getPaddingLeft(),
                    binding.getRoot().getPaddingTop() + getStatusBarHeight(),
                    binding.getRoot().getPaddingRight(),
                    binding.getRoot().getPaddingBottom()
            );
        }
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
    protected void onRestart() {
        super.onRestart();
        isResume = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            if (!(this instanceof SplashActivity) && !(this instanceof ScreenRecorderActivity) && !(this instanceof ScreenshotActivity)) {
                resumeResultLauncher.launch(new Intent(this, WelcomeBackActivity.class));
            }
        }
        if (ConstantRemote.resume && ConstantRemote.show_ads) {
            AppOpenManager.getInstance().enableAppResumeWithActivity(getClass());
        } else {
            AppOpenManager.getInstance().disableAppResumeWithActivity(getClass());
        }


    }

    public ActivityResultLauncher<Intent> resumeResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == SPUtils.WELCOME) {

        } else {

        }
    });

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

}
