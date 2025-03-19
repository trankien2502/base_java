package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFloatingIconBinding;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;

public class FloatingIconActivity extends BaseActivity<ActivityFloatingIconBinding> {

    Handler handler = new Handler();
    Runnable runnableNativeAds;

    @Override
    public ActivityFloatingIconBinding getBinding() {
        return ActivityFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativeFloatingAds();
        EventTracking.logEvent(getBaseContext(), "floating_icon_view");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ItemFunctionIcon> list = SPUtils.getListFloatingIcon();
        ItemFunctionIcon itemSingleTap = SPUtils.getObject(this, SPUtils.FLOATING_ICON_SINGLE_TAP, list.get(3));
        ItemFunctionIcon itemDoubleTap = SPUtils.getObject(this, SPUtils.FLOATING_ICON_DOUBLE_TAP, list.get(0));
        ItemFunctionIcon itemLongPress = SPUtils.getObject(this, SPUtils.FLOATING_ICON_LONG_PRESS, list.get(0));
        binding.tvSingleTap.setText(itemSingleTap.getText());
        binding.tvDoubleTap.setText(itemDoubleTap.getText());
        binding.tvLongPress.setText(itemLongPress.getText());
    }

    @Override
    public void bindView() {
        binding.clIconStyle.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_icon_style_click");
            showInterFloating(new Intent(this, IconStyleActivity.class));
        });
        binding.clSingleTap.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_single_tap_click");
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION, SPUtils.FLOATING_ICON_SINGLE_TAP);
            showInterFloating(intent);
        });
        binding.clDoubleTap.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_double_tap_click");
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION, SPUtils.FLOATING_ICON_DOUBLE_TAP);
            showInterFloating(intent);
        });
        binding.clLongPress.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_long_press_click");
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION, SPUtils.FLOATING_ICON_LONG_PRESS);
            showInterFloating(intent);
        });
        binding.ivBack.setOnClickListener(v -> {

            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_done_click");
        });
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "floating_icon_back_click");
        setResult(RESULT_OK);
        finish();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == RESULT_OK) {
            loadNativeFloatingAds();
        } else if (result.getResultCode() == 2502) {
            loadNativeFloatingAds();
            binding.llSuccess.setVisibility(VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.llSuccess.setVisibility(GONE);
                }
            }, 1500);
        }
    });
    private void loadInterFloating() {
        if (ConstantIdAds.mInterFloating == null && IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterFloating.isEmpty() && ConstantRemote.inter_floating  && ConstantRemote.show_ads) {
            ConstantIdAds.mInterFloating = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterFloating);
        }
    }
    private void showInterFloating(Intent intent) {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterFloating.isEmpty() && ConstantRemote.inter_floating && ConstantRemote.show_ads) {
            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start_old > ConstantRemote.interval_interstitial_from_start * 1000) {
                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
                    try {
                        if (ConstantIdAds.mInterFloating != null) {
                            CommonAd.getInstance().forceShowInterstitialByTime(this, ConstantIdAds.mInterFloating, new CommonAdCallback() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    resultLauncher.launch(intent);
                                }

                                @Override
                                public void onAdClosedByTime() {
                                    super.onAdClosedByTime();
                                    ConstantIdAds.mInterFloating = null;
                                    ConstantRemote.time_interval_old = System.currentTimeMillis();
                                    loadInterFloating();
                                }
                            }, true);
                        } else {
                            loadInterFloating();
                        }
                    } catch (Exception e) {
                        resultLauncher.launch(intent);
                    }
                } else {
                    resultLauncher.launch(intent);
                }
            } else {
                resultLauncher.launch(intent);
            }
        } else {
            resultLauncher.launch(intent);
        }
    }
    public void loadNativeFloatingAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeFloating.isEmpty() && ConstantRemote.native_floating  && ConstantRemote.show_ads) {
                handler.removeCallbacks(runnableNativeAds);
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativeFloatingAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativeFloating.removeAllViews();
                binding.nativeFloating.addView(adViewLoad);
                binding.nativeFloating.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeFloating, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativeFloating.removeAllViews();
                                binding.nativeFloating.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativeFloating.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativeFloating.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeFloating.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
    }
}