package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityCustomMenuBinding;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorPickerDialog;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorSelectCallBack;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class CustomMenuActivity extends BaseActivity<ActivityCustomMenuBinding> {

    MenuAdapter menuAdapter;
    int oldColor;
    int currentColor;
    Handler handler = new Handler();
    Runnable runnableNativeAds;
    Runnable runnableNativeDialogAds;
    ColorPickerDialog colorDialog;

    @Override
    public ActivityCustomMenuBinding getBinding() {
        return ActivityCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativeCustomAds();
        EventTracking.logEvent(this, "custom_menu_view");
        oldColor = SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, Color.parseColor("#cc000000"));
        currentColor = oldColor;
        menuAdapter = new MenuAdapter(this);
        binding.viewPager.setAdapter(menuAdapter);
        binding.viewPager.setCurrentItem(1);
        binding.viewPager.setCurrentItem(0);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {

            onBack();
        });
        binding.menu1.setOnClickListener(v -> {
            EventTracking.logEvent(this, "custom_menu_back_features_click");
            binding.viewPager.setCurrentItem(0);
            changeState(0);
        });
        binding.menu2.setOnClickListener(v -> {
            EventTracking.logEvent(this, "custom_menu_next_features_click");
            binding.viewPager.setCurrentItem(1);
            changeState(1);
        });
        binding.llColor.setOnClickListener(v -> {
            EventTracking.logEvent(this, "custom_menu_color_click");
            showColorPickerDialog();
        });
        binding.llRestore.setOnClickListener(v -> {
            EventTracking.logEvent(this, "custom_menu_restore_click");
            if (binding.viewPager.getCurrentItem() == 0) {
                if (Menu1Fragment.instance != null) {
                    Menu1Fragment.instance.restore();
                }
                Log.e("menu_check", "click1");
            } else {
                if (Menu2Fragment.instance != null)
                    Menu2Fragment.instance.restore();
                Log.e("menu_check", "click2");
            }

        });
        binding.ivGone.setOnClickListener(v -> {
            EventTracking.logEvent(this, "custom_menu_done_click");
            SPUtils.setList(this, SPUtils.MENU_FUNCTION_1, Menu1Fragment.instance.listCurrent);
            SPUtils.setList(this, SPUtils.MENU_FUNCTION_2, Menu2Fragment.instance.listCurrent);
            SPUtils.setInt(this, SPUtils.MENU_BACKGROUND_COLOR, currentColor);
            onBack();
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeState(position);
            }
        });
    }

    private void showColorPickerDialog() {
        colorDialog = new ColorPickerDialog(this, true, currentColor);
        colorDialog.init(new ColorSelectCallBack() {
            @Override
            public void select(int color) {
                currentColor = color;
                Menu1Fragment.instance.binding.backgroundMenu.setBgColorLight(color);
                Menu2Fragment.instance.binding.backgroundMenu.setBgColorLight(color);
            }
        });
        colorDialog.show();
        loadNativePopupColorAds();
        colorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnableNativeDialogAds);
            }
        });
    }
    public void loadNativePopupColorAds() {
        if (colorDialog != null && colorDialog.isShowing()) {
            try {
                if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativePopup.isEmpty() && ConstantRemote.native_popup && ConstantRemote.show_ads) {
                    handler.removeCallbacks(runnableNativeDialogAds);
                    runnableNativeDialogAds = new Runnable() {
                        @Override
                        public void run() {
                            loadNativePopupColorAds();
                        }
                    };
                    @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                    colorDialog.binding.nativePopup.removeAllViews();
                    colorDialog.binding.nativePopup.addView(adViewLoad);
                    colorDialog.binding.nativePopup.setVisibility(View.VISIBLE);
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativePopup, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                            colorDialog.binding.nativePopup.removeAllViews();
                            colorDialog.binding.nativePopup.addView(adView);
                            Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            if (ConstantRemote.time_native_reload != 0)
                                handler.postDelayed(runnableNativeDialogAds, ConstantRemote.time_native_reload * 1000);
                            CheckAds.getInstance().checkAds(adView, CheckAds.OT);

                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            colorDialog.binding.nativePopup.setVisibility(View.GONE);
                        }
                    });

                } else {
                    colorDialog.binding.nativePopup.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                colorDialog.binding.nativePopup.setVisibility(View.GONE);
            }
        }

    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == RESULT_OK) {
            loadNativeCustomAds();
        }
    });

    private void changeState(int position) {
        if (position == 0) {
            binding.menu1.setBackgroundResource(R.drawable.bg_menu_s);
            binding.menu1.setTextColor(Color.WHITE);
            binding.menu2.setBackgroundResource(R.drawable.bg_menu_sn);
            binding.menu2.setTextColor(Color.parseColor("#8a8a8a"));
        } else {
            binding.menu2.setBackgroundResource(R.drawable.bg_menu_s);
            binding.menu2.setTextColor(Color.WHITE);
            binding.menu1.setBackgroundResource(R.drawable.bg_menu_sn);
            binding.menu1.setTextColor(Color.parseColor("#8a8a8a"));
        }
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(this, "custom_menu_back_click");
        setResult(RESULT_OK);
        finish();
    }

    public void loadNativeCustomAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeMenu.isEmpty() && ConstantRemote.native_menu  && ConstantRemote.show_ads) {
                handler.removeCallbacks(runnableNativeAds);
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativeCustomAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativeCustom.removeAllViews();
                binding.nativeCustom.addView(adViewLoad);
                binding.nativeCustom.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeMenu, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativeCustom.removeAllViews();
                                binding.nativeCustom.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativeCustom.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativeCustom.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeCustom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
    }
}