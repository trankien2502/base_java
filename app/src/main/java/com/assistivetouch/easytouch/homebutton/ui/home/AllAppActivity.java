package com.assistivetouch.easytouch.homebutton.ui.home;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityAllAppBinding;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityWelcomeBackBinding;
import com.assistivetouch.easytouch.homebutton.dialog.LoadingDialog;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppCallBack;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfo;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfoAdapter;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllAppActivity extends AppCompatActivity {

    ActivityAllAppBinding binding;
    ItemAppInfoAdapter adapter;
    List<ItemAppInfo> list = new ArrayList<>();
    private PackageManager packageManager;
    LoadingDialog dialog;
    boolean isAllApp = true;
    int favouritePosition;
    ArrayList<ItemAppInfo> favouriteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppOpenManager.getInstance().disableAppResumeWithActivity(AllAppActivity.class);
        super.onCreate(savedInstanceState);
        binding = ActivityAllAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        });
        binding.getRoot().setPadding(
                binding.getRoot().getPaddingLeft(),
                binding.getRoot().getPaddingTop() + getStatusBarHeight(),
                binding.getRoot().getPaddingRight(),
                binding.getRoot().getPaddingBottom()
        );
        hideNavigation();
        initView();
        bindView();
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void initView() {
        loadBanner();
        favouriteList = SPUtils.getListFavourite(this, SPUtils.FAVOURITE_APP, SPUtils.getListDefaultFavourite(this));
        isAllApp = getIntent().getBooleanExtra(SPUtils.INTENT_ALL_APP, true);
        favouritePosition = getIntent().getIntExtra(SPUtils.FAVOURITE_POSITION, 0);
        showLoadingDialog();
        new Thread(() -> {
            getInstalledApps();
            runOnUiThread(() -> {
                dismissLoadingDialog();
                adapter = new ItemAppInfoAdapter(this, list, new ItemAppCallBack() {
                    @Override
                    public void select(ItemAppInfo appInfo) {
                        if (isAllApp) {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                            if (launchIntent != null) {
                                startActivity(launchIntent);
                            } else {
                                Toast.makeText(getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                            if (launchIntent != null) {
                                appInfo.setId(favouritePosition);
                                appInfo.setIcon(null);
                                favouriteList.set(favouritePosition, appInfo);
                                SPUtils.setListFavourite(getBaseContext(), SPUtils.FAVOURITE_APP, favouriteList);
                                if (ServiceScreen.instance != null)
                                    ServiceScreen.instance.showFavourite();
                                onBack();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                binding.rcvApp.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppOpenManager.getInstance().disableAppResumeWithActivity(AllAppActivity.class);
    }

    private void loadBanner() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsBannerAll.isEmpty() && ConstantRemote.banner_all && ConstantRemote.show_ads) {
            new Thread(() -> {
                runOnUiThread(() -> {
                    binding.rlBanner.setVisibility(View.VISIBLE);
                    binding.rlBanner.removeAllViews();
                    RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_banner_control, null, false);
                    binding.rlBanner.addView(layout);
                    Admob.getInstance().loadBannerFloor(this, ConstantIdAds.listIDAdsBannerAll);
                });
            }).start();
        } else {
            binding.rlBanner.setVisibility(View.GONE);
        }
    }
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
    }

    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void getInstalledApps() {
        if (packageManager == null) {
            packageManager = getPackageManager(); // Khởi tạo lại nếu cần
        }
        list.clear();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // Lọc ứng dụng hệ thống
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                String packageName = packageInfo.packageName;
                Drawable icon = packageManager.getApplicationIcon(appInfo);
                list.add(new ItemAppInfo(appName, packageName, icon));
            }
        }
    }

    private void showLoadingDialog() {
        dialog = new LoadingDialog(this, false);
        dialog.show();
    }

    private void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
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