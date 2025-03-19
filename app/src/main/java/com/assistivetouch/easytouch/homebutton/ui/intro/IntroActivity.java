package com.assistivetouch.easytouch.homebutton.ui.intro;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityIntroBinding;
import com.assistivetouch.easytouch.homebutton.dialog.rate.IClickDialogRate;
import com.assistivetouch.easytouch.homebutton.dialog.rate.RatingDialog;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.permission.PermissionActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "5", "9"));
    ImageView[] dots = null;
    int positionPage = 0;
    String[] title;

    List<Integer> listImage;
    SlideAdapter adapter;
    int height;
    private boolean isShowRate = false;
    RatingDialog ratingDialog;
    Handler handler = new Handler();
    Runnable runnableNativeDialogAds;


    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadInterIntro();
        loadNativeIntroAds();
        listImage = new ArrayList<>();
        if (IsNetWork.haveNetworkConnection(this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full  && ConstantRemote.show_ads) {
            binding.circle4.setVisibility(View.VISIBLE);
            listImage.add(R.drawable.img_intro_1);
            listImage.add(R.drawable.img_intro_2);
            listImage.add(R.drawable.img_intro_3);
            listImage.add(R.drawable.img_intro_3);
            dots = new ImageView[]{findViewById(R.id.circle1), findViewById(R.id.circle2), findViewById(R.id.circle3), findViewById(R.id.circle4)};
            title = new String[]{getResources().getString(R.string.intro_1), getResources().getString(R.string.intro_2), getResources().getString(R.string.intro_3), getResources().getString(R.string.intro_3)};
        } else {
            binding.circle4.setVisibility(View.GONE);
            listImage.add(R.drawable.img_intro_1);
            listImage.add(R.drawable.img_intro_2);
            listImage.add(R.drawable.img_intro_3);
            dots = new ImageView[]{findViewById(R.id.circle1), findViewById(R.id.circle2), findViewById(R.id.circle3)};
            title = new String[]{getResources().getString(R.string.intro_1), getResources().getString(R.string.intro_2), getResources().getString(R.string.intro_3)};
        }
        binding.viewHeight.post(() -> {
            height = binding.viewHeight.getHeight();
            Log.d("intro_check", "Chiều cao của viewHeight: " + height);
            adapter = new SlideAdapter(this, this, listImage
                    , height, () -> {
                binding.viewPager.setCurrentItem(3);
            });
            binding.viewPager.setAdapter(adapter);
            binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    positionPage = position;
                    if (!IsNetWork.haveNetworkConnectionUMP(IntroActivity.this)) {
                        if (IntroActivity.this.listImage.size() > 3) {
                            listImage.remove(3);
                            adapter.setList(listImage);
                        }
                    }
                    if (IsNetWork.haveNetworkConnection(IntroActivity.this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full  && ConstantRemote.show_ads) {
                        if (positionOffset > 0) {
                            binding.clIntro.setVisibility(View.GONE);
                        } else {
                            if (position == 2) binding.clIntro.setVisibility(View.GONE);
                            else binding.clIntro.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    changeContentInit(position);
                    if (IsNetWork.haveNetworkConnection(IntroActivity.this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full  && ConstantRemote.show_ads) {
                        if (position == 0) {
                            EventTracking.logEvent(IntroActivity.this, "Intro1_view");
                        } else if (position == 1) {
                            EventTracking.logEvent(IntroActivity.this, "Intro2_view");
                        } else if (position == 3) {
                            EventTracking.logEvent(IntroActivity.this, "Intro3_view");
                        }
                    } else {
                        if (position == 0) {
                            EventTracking.logEvent(IntroActivity.this, "Intro1_view");
                        } else if (position == 1) {
                            EventTracking.logEvent(IntroActivity.this, "Intro2_view");
                        } else {
                            EventTracking.logEvent(IntroActivity.this, "Intro3_view");
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
        });


    }


    @Override
    public void bindView() {
        binding.btnNext2.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() == 0) {
                EventTracking.logEvent(IntroActivity.this, "Intro1_next_click");
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else if (binding.viewPager.getCurrentItem() == 1) {
                EventTracking.logEvent(IntroActivity.this, "Intro2_next_click");
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                EventTracking.logEvent(IntroActivity.this, "Intro3_next_click");
                if (!SharePrefUtils.isRated(this)) {
                    if (exitRate.contains(String.valueOf(SharePrefUtils.getCountOpenApp(this)))) {
                        if (!isShowRate) rateApp();
                        else showInterIntro();
                    } else {
                        showInterIntro();
                    }
                } else {
                    showInterIntro();
                }
            }
        });
    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    public void goToHome() {
        startNextActivity(PermissionActivity.class, null);
        finish();
    }

    private void changeContentInit(int position) {
        binding.tvIntro.setText(title[position]);
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full  && ConstantRemote.show_ads) {
            for (int i = 0; i < 4; i++) {
                if (i == position) {
                    dots[i].setImageResource(R.drawable.ic_intro_s);
                } else dots[i].setImageResource(R.drawable.ic_intro_sn);
            }
        } else {
            if (binding.circle4.getVisibility() == View.VISIBLE) {
                binding.circle4.setVisibility(View.GONE);
            }
            for (int i = 0; i < 3; i++) {
                if (i == position) {
                    dots[i].setImageResource(R.drawable.ic_intro_s);
                } else dots[i].setImageResource(R.drawable.ic_intro_sn);
            }
        }
    }


    @Override
    protected void onResume() {
        AppOpenManager.getInstance().enableAppResumeWithActivity(IntroActivity.class);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager.getCurrentItem());
    }

    private void rateApp() {
        ratingDialog = new RatingDialog(this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    AppOpenManager.getInstance().disableAppResumeWithActivity(IntroActivity.class);
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(IntroActivity.this);
                    int star = SPUtils.getInt(IntroActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(IntroActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(IntroActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(IntroActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(IntroActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            int star = SPUtils.getInt(IntroActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(IntroActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            SharePrefUtils.forceRated(IntroActivity.this);
                            ratingDialog.dismiss();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(IntroActivity.this, "rate_not_now");
                ratingDialog.dismiss();
            }

        });
        ratingDialog.show();
        loadNativePopupRateAds();
        ratingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnableNativeDialogAds);
            }
        });
        isShowRate = true;
        EventTracking.logEvent(this, "rate_show");
    }

    public void loadNativeIntroAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this)) {
                if (!ConstantIdAds.listIDAdsNativeIntro.isEmpty() && ConstantRemote.native_intro  && ConstantRemote.show_ads) {
                    @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(IntroActivity.this).inflate(R.layout.layout_native_load_small_cta_above, null);
                    binding.nativeIntro.removeAllViews();
                    binding.nativeIntro.addView(adView);
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeIntro, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(IntroActivity.this).inflate(R.layout.layout_native_show_small_cta_above, null);
                            binding.nativeIntro.removeAllViews();
                            binding.nativeIntro.addView(adView);
                            Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            CheckAds.checkAds(adView, CheckAds.IN);
                        }

                        @Override
                        public void onAdFailedToLoad(@Nullable LoadAdError i) {
                            binding.nativeIntro.removeAllViews();
                        }
                    });
                } else {
                    binding.nativeIntro.removeAllViews();
                }
            } else {
                binding.nativeIntro.removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeIntro.removeAllViews();
        }
    }

    private void showInterIntro() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterIntro.isEmpty() && ConstantRemote.inter_intro  && ConstantRemote.show_ads) {
            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start_old > ConstantRemote.interval_interstitial_from_start * 1000) {
                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
                    try {
                        if (ConstantIdAds.mInterIntro != null) {
                            CommonAd.getInstance().forceShowInterstitialByTime(this, ConstantIdAds.mInterIntro, new CommonAdCallback() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    goToHome();
                                }

                                @Override
                                public void onAdClosedByTime() {
                                    super.onAdClosedByTime();
                                    ConstantRemote.time_interval_old = System.currentTimeMillis();
                                    loadInterIntro();
                                }
                            }, true);
                        } else {
                            loadInterIntro();
                        }
                    } catch (Exception e) {
                        goToHome();
                    }
                } else {
                    goToHome();
                }
            } else {
                goToHome();
            }
        } else {
            goToHome();
        }
    }

    private void loadInterIntro() {
        if (ConstantIdAds.mInterIntro == null && IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterIntro.isEmpty() && ConstantRemote.inter_intro  && ConstantRemote.show_ads) {
            ConstantIdAds.mInterIntro = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterIntro);
        }
    }
    public void loadNativePopupRateAds() {
        if (ratingDialog!=null && ratingDialog.isShowing()){
            try {
                if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativePopup.isEmpty() && ConstantRemote.native_popup && ConstantRemote.show_ads) {
                    handler.removeCallbacks(runnableNativeDialogAds);
                    runnableNativeDialogAds = new Runnable() {
                        @Override
                        public void run() {
                            loadNativePopupRateAds();
                        }
                    };
                    @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                    ratingDialog.binding.nativePopup.removeAllViews();
                    ratingDialog.binding.nativePopup.addView(adViewLoad);
                    ratingDialog.binding.nativePopup.setVisibility(View.VISIBLE);
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativePopup, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                            ratingDialog.binding.nativePopup.removeAllViews();
                            ratingDialog.binding.nativePopup.addView(adView);
                            Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                            if (ConstantRemote.time_native_reload != 0)
                                handler.postDelayed(runnableNativeDialogAds, ConstantRemote.time_native_reload * 1000);
                            CheckAds.getInstance().checkAds(adView, CheckAds.OT);

                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            ratingDialog.binding.nativePopup.setVisibility(View.GONE);
                        }
                    });

                } else {
                    ratingDialog.binding.nativePopup.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                ratingDialog.binding.nativePopup.setVisibility(View.GONE);
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
