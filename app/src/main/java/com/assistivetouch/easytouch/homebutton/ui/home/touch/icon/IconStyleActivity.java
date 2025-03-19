package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityIconStyleBinding;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;

public class IconStyleActivity extends BaseActivity<ActivityIconStyleBinding> {

    IconStyleAdapter iconStyleAdapter;
    List<IconStyle> iconStyleList = new ArrayList<>();
    int oldIconSource, currentIconSource;
    Handler handler = new Handler();
    Runnable runnableNativeAds;

    @Override
    public ActivityIconStyleBinding getBinding() {
        return ActivityIconStyleBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativeFloatingAds();
        EventTracking.logEvent(getBaseContext(), "floating_icon_style_view");
        initData();
        iconStyleAdapter = new IconStyleAdapter(this, iconStyleList, new IconStyleCallBack() {
            @Override
            public void select(IconStyle iconStyle) {
                EventTracking.logEvent(getBaseContext(), "floating_icon_style_item_click");
                if (ServiceScreen.instance != null)
                    ServiceScreen.instance.setIconStyle(iconStyle.getSource());
                currentIconSource = iconStyle.getSource();
            }
        });
        binding.rcvIcon.setAdapter(iconStyleAdapter);
    }

    private void initData() {
        iconStyleList.add(new IconStyle(R.drawable.icon_1));
        iconStyleList.add(new IconStyle(R.drawable.icon_2));
        iconStyleList.add(new IconStyle(R.drawable.icon_3));
        iconStyleList.add(new IconStyle(R.drawable.icon_4));
        iconStyleList.add(new IconStyle(R.drawable.icon_5));
        iconStyleList.add(new IconStyle(R.drawable.icon_6));
        iconStyleList.add(new IconStyle(R.drawable.icon_7));
        iconStyleList.add(new IconStyle(R.drawable.icon_8));
        iconStyleList.add(new IconStyle(R.drawable.icon_9));
        iconStyleList.add(new IconStyle(R.drawable.icon_10));
        iconStyleList.add(new IconStyle(R.drawable.icon_11));
        iconStyleList.add(new IconStyle(R.drawable.icon_12));
        iconStyleList.add(new IconStyle(R.drawable.icon_13));
        iconStyleList.add(new IconStyle(R.drawable.icon_14));
        iconStyleList.add(new IconStyle(R.drawable.icon_15));
        iconStyleList.add(new IconStyle(R.drawable.icon_16));
        iconStyleList.add(new IconStyle(R.drawable.icon_17));
        iconStyleList.add(new IconStyle(R.drawable.icon_18));
        iconStyleList.add(new IconStyle(R.drawable.icon_19));
        iconStyleList.add(new IconStyle(R.drawable.icon_20));
        iconStyleList.add(new IconStyle(R.drawable.icon_21));
        iconStyleList.add(new IconStyle(R.drawable.icon_22));
        iconStyleList.add(new IconStyle(R.drawable.icon_23));
        iconStyleList.add(new IconStyle(R.drawable.icon_24));
        oldIconSource = SPUtils.getInt(this, SPUtils.ICON_STYLE, R.drawable.icon_1);
        for (IconStyle iconStyle : iconStyleList)
            iconStyle.setSelect(iconStyle.getSource() == oldIconSource);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {

            if (ServiceScreen.instance != null)
                ServiceScreen.instance.setIconStyle(oldIconSource);
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "floating_icon_style_done_click");
            SPUtils.setInt(this, SPUtils.ICON_STYLE, currentIconSource);
            onBack();
        });
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "floating_icon_style_back_click");
        setResult(RESULT_OK);
        finish();
    }

    public void loadNativeFloatingAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeFloating.isEmpty() && ConstantRemote.native_floating  && ConstantRemote.show_ads) {
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