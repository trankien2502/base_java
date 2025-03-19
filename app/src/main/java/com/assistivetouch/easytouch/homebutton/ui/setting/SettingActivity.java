package com.assistivetouch.easytouch.homebutton.ui.setting;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivitySettingBinding;
import com.assistivetouch.easytouch.homebutton.dialog.rate.IClickDialogRate;
import com.assistivetouch.easytouch.homebutton.dialog.rate.RatingDialog;
import com.assistivetouch.easytouch.homebutton.ui.language.LanguageActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    RatingDialog ratingDialog;
    Handler handler = new Handler();
    Runnable runnableNativeDialogAds;

    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "setting_view");
        binding.tvTitle.setText(R.string.settings);
        binding.tvLangCurrent.setText(SPUtils.getString(this, SPUtils.LANGUAGE, ""));
        if (SharePrefUtils.isRated(this)) {
            binding.clRate.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        binding.clLanguage.setOnClickListener(view -> {
            EventTracking.logEvent(this, "setting_language_click");
            resultLauncher.launch(new Intent(this, LanguageActivity.class));
        });
        binding.ivBack.setOnClickListener(view -> onBack());
        binding.clRate.setOnClickListener(view -> onRate());
        binding.clAbout.setOnClickListener(view -> {
            EventTracking.logEvent(this, "setting_about_click");
            resultLauncher.launch(new Intent(this, AboutActivity.class));
        });
        binding.clShare.setOnClickListener(view -> onShare());
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == RESULT_OK) {
            Log.d("activity_check", "home");
        }
    });

    private void onRate() {
        EventTracking.logEvent(this, "setting_rate_click");
        ratingDialog = new RatingDialog(SettingActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                binding.clRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    resultLauncher.launch(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    int star = SPUtils.getInt(SettingActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(SettingActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                    AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
                    SharePrefUtils.forceRated(SettingActivity.this);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                EventTracking.logEvent(SettingActivity.this, "rate_submit");
                ReviewManager manager = ReviewManagerFactory.create(SettingActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            int star = SPUtils.getInt(SettingActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(SettingActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            binding.clRate.setVisibility(View.GONE);
                            SharePrefUtils.forceRated(SettingActivity.this);
                            ratingDialog.dismiss();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(SettingActivity.this, "rate_not_now");
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
        EventTracking.logEvent(this, "rate_show");
    }

    public void loadNativePopupRateAds() {
        if (ratingDialog != null && ratingDialog.isShowing()) {
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

    private void onShare() {
        EventTracking.logEvent(this, "setting_share_click");
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        resultLauncher.launch(Intent.createChooser(intentShare, "Share with"));
        AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}