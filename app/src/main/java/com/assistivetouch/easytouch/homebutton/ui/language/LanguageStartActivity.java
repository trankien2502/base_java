package com.assistivetouch.easytouch.homebutton.ui.language;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.ui.intro.IntroActivity;
import com.assistivetouch.easytouch.homebutton.ui.language.adapter.LanguageStartAdapter;
import com.assistivetouch.easytouch.homebutton.ui.language.model.LanguageModel;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityLanguageStartBinding;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageStartActivity extends BaseActivity<ActivityLanguageStartBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;
    String nameLang;
    Handler handler = new Handler();
    Runnable runnableNativeAds;
    private boolean isLoadNative = true;

    @Override
    public ActivityLanguageStartBinding getBinding() {
        return ActivityLanguageStartBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadNativeAds();
        EventTracking.logEvent(this, "language_fo_open");
        initData();
        binding.tvTitle.setText(getString(R.string.language));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
        }, this);
        binding.rcvLangStart.setLayoutManager(linearLayoutManager);
        binding.rcvLangStart.setAdapter(languageStartAdapter);
    }

    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(view -> {
            EventTracking.logEvent(this, "language_fo_save_click");
            if (codeLang == null || codeLang.isEmpty()) {
                Toast.makeText(this, R.string.please_select_a_language, Toast.LENGTH_SHORT).show();
                return;
            }
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SPUtils.setString(this, SPUtils.LANGUAGE, nameLang);
            startNextActivity(IntroActivity.class, null);
            finishAffinity();
        });
    }

    public void loadNativeAds() {
        if (isLoadNative) {
            try {
                if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeLanguage.isEmpty() && ConstantRemote.native_language  && ConstantRemote.show_ads) {
                    isLoadNative = false;
                    handler.removeCallbacks(runnableNativeAds);
                    runnableNativeAds = new Runnable() {
                        @Override
                        public void run() {
                            loadNativeAds();
                        }
                    };
                    @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(LanguageStartActivity.this).inflate(R.layout.layout_native_load_large_cta_above, null);
                    binding.nativeLanguage.removeAllViews();
                    binding.nativeLanguage.addView(adViewLoad);
                    binding.nativeLanguage.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeLanguage, new AdCallback() {
                            @Override
                            public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                                runOnUiThread(() -> {
                                    @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(LanguageStartActivity.this).inflate(R.layout.layout_native_show_large_cta_above, null);
                                    binding.nativeLanguage.removeAllViews();
                                    binding.nativeLanguage.addView(adView);
                                    Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                    if (ConstantRemote.time_native_reload * 1000 != 0)
                                        handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                    isLoadNative = true;
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                runOnUiThread(() -> {
                                    binding.nativeLanguage.setVisibility(View.GONE);
                                });

                            }
                        });
                    }).start();
                } else {
                    binding.nativeLanguage.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                binding.nativeLanguage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
    }
}
