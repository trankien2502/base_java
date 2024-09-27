package com.tkt.spin_wheel.ui.intro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.home.HomeActivity;
import com.tkt.spin_wheel.ui.permission.PermissionActivity;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ActivityIntroBinding;
import com.tkt.spin_wheel.util.SharePrefUtils;


public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    ImageView[] dots = null;
    int[] listIntroTitle = null, listIntroContent = null;
    IntroAdapter introAdapter;

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        dots = new ImageView[]{binding.cricle1, binding.cricle2, binding.cricle3};
        listIntroTitle = new int[]{R.string.intro_1, R.string.intro_2, R.string.intro_3};
        listIntroContent = new int[]{R.string.content_intro_1, R.string.content_intro_2, R.string.content_intro_3};
        introAdapter = new IntroAdapter(this);

        binding.viewPager2.setAdapter(introAdapter);

        binding.viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeContentInit(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        loadNativeIntro();
//        loadInterIntro();
    }

    //




    @Override
    public void bindView() {
        binding.clNext.setOnClickListener(view -> {
            switch (binding.viewPager2.getCurrentItem()) {
                case 0:
                    EventTracking.logEvent(this, "onboarding1_next_click");
                    break;
                case 1:
                    EventTracking.logEvent(this, "onboarding2_next_click");
                    break;
                case 2:
                    EventTracking.logEvent(this, "onboarding3_next_click");
                    break;

            }
            if (binding.viewPager2.getCurrentItem() < 2) {
                binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
            } else {
                //showInterIntro();
                startNextActivity();
            }
        });
        binding.btnNext2.setOnClickListener(v -> {
//            showInterIntro();
            startNextActivity();
        });

    }

    private void changeContentInit(int position) {
        binding.tvIntro.setText(listIntroTitle[position]);
        binding.tvIntroContent.setText(listIntroContent[position]);
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                dots[i].setImageResource(R.drawable.ic_intro_s);

            } else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }
        switch (position) {
            case 0:
                binding.clNext.setVisibility(View.VISIBLE);
                binding.rlBottom3.setVisibility(View.GONE);
                binding.rlBottom.setVisibility(View.VISIBLE);
                binding.nativeLoad.setVisibility(View.GONE);
                binding.nativeIntro.setVisibility(View.GONE);
                EventTracking.logEvent(this, "Onboarding1_view");
                break;
            case 1:
                binding.clNext.setVisibility(View.VISIBLE);
                binding.rlBottom3.setVisibility(View.GONE);
                binding.rlBottom.setVisibility(View.VISIBLE);
                binding.nativeLoad.setVisibility(View.GONE);
                binding.nativeIntro.setVisibility(View.GONE);
                EventTracking.logEvent(this, "Onboarding2_view");
                break;
            case 2:
                binding.clNext.setVisibility(View.GONE);
                binding.rlBottom.setVisibility(View.GONE);
                binding.rlBottom3.setVisibility(View.VISIBLE);
                binding.nativeLoad.setVisibility(View.GONE);
                binding.nativeIntro.setVisibility(View.GONE);
//                if (IsNetWork.haveNetworkConnection(IntroActivity.this) && ConstantIdAds.listIDAdsNativeIntro.size() != 0 && ConstantRemote.native_intro) {
//                    binding.nativeLoad.setVisibility(View.VISIBLE);
//                    new Handler().postDelayed(() -> {
//                        binding.nativeLoad.setVisibility(View.GONE);
//                        binding.nativeIntro.setVisibility(View.VISIBLE);
//
//                    }, 500);
//                }
                EventTracking.logEvent(this, "Onboarding3_view");
                break;

        }
    }

    public void startNextActivity() {
        if (SharePrefUtils.getCountOpenApp(IntroActivity.this) == 1) {
            startNextActivity(PermissionActivity.class, null);
        } else {
            if (!checkStoragePermission()) {
                startNextActivity(PermissionActivity.class, null);
            } else
                startNextActivity(HomeActivity.class, null);
        }
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager2.getCurrentItem());
    }
//    public void loadNativeIntro() {
//        try {
//            if (IsNetWork.haveNetworkConnection(IntroActivity.this) && ConstantIdAds.listIDAdsNativeIntro.size() != 0 && ConstantRemote.native_intro  && CheckAds.getInstance().isShowAds(this)) {
//                Admob.getInstance().loadNativeAd(IntroActivity.this, ConstantIdAds.listIDAdsNativeIntro, new AdCallback() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
//                        NativeAdView adView = (NativeAdView) LayoutInflater.from(IntroActivity.this).inflate(R.layout.layout_native_show_small, null);
//                        binding.nativeIntro.removeAllViews();
//                        binding.nativeIntro.addView(adView);
//                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                        CheckAds.getInstance().checkAds(adView, CheckAds.IN);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                        binding.nativeIntro.setVisibility(View.GONE);
//                    }
//                });
//            } else {
//                binding.nativeIntro.setVisibility(View.GONE);
//            }
//
//        } catch (Exception e) {
//            binding.nativeIntro.setVisibility(View.GONE);
//        }
//    }
//    private void loadInterIntro() {
//        if (ConstantIdAds.mInterIntro == null && IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsInterIntro.size() != 0 && ConstantRemote.inter_intro  && CheckAds.getInstance().isShowAds(this)) {
//            ConstantIdAds.mInterIntro = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterIntro);
//        }
//    }
//
//    private void showInterIntro() {
//        if (IsNetWork.haveNetworkConnectionUMP(IntroActivity.this) && ConstantIdAds.listIDAdsInterIntro.size() != 0 && ConstantRemote.inter_intro  && CheckAds.getInstance().isShowAds(this)) {
//            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start_old > ConstantRemote.interval_interstitial_from_start * 1000) {
//                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
//                    try {
//                        if (ConstantIdAds.mInterIntro != null) {
//                            CommonAd.getInstance().forceShowInterstitialByTime(this, ConstantIdAds.mInterIntro, new CommonAdCallback() {
//                                @Override
//                                public void onAdClosed() {
//                                    super.onAdClosed();
//                                    startNextActivity();
//                                }
//
//                                @Override
//                                public void onAdClosedByTime() {
//                                    super.onAdClosedByTime();
//                                    startNextActivity();
//                                    ConstantIdAds.mInterIntro = null;
//                                    loadInterIntro();
//                                }
//                            }, true);
//                        } else {
//                            startNextActivity();
//                            ConstantIdAds.mInterIntro = null;
//                            loadInterIntro();
//                        }
//                    } catch (Exception e) {
//                        startNextActivity();
//                    }
//                } else {
//                    startNextActivity();
//                }
//            }else {
//                startNextActivity();
//            }
//        }else {
//            startNextActivity();
//        }
//    }
}