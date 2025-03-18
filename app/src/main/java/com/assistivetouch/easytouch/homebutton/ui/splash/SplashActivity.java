package com.assistivetouch.easytouch.homebutton.ui.splash;

import static com.ads.sapp.util.GoogleMobileAdsConsentManager.getConsentResult;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.BannerCallback;
import com.ads.sapp.util.CheckAds;
import com.ads.sapp.util.GoogleMobileAdsConsentManager;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.assistivetouch.easytouch.homebutton.BuildConfig;
import com.assistivetouch.easytouch.homebutton.MyApplication;
import com.assistivetouch.easytouch.homebutton.ads.AdsModel;
import com.assistivetouch.easytouch.homebutton.ads.ApiService;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivitySplashBinding;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.intro.IntroActivity;
import com.assistivetouch.easytouch.homebutton.ui.language.LanguageStartActivity;
import com.assistivetouch.easytouch.homebutton.ui.permission.PermissionActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    AdCallback adCallback;
    GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    boolean cancelCallApi = false;
    Thread threadCallApi;

    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);
        if (!IsNetWork.haveNetworkConnectionUMP(this)) binding.rlBanner.setVisibility(View.GONE);
        EventTracking.logEvent(getBaseContext(), "splash_open");
        AppOpenManager.getInstance().disableAppResume();
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        clearAllAds();
        startThreadToCancelApi();
        callUMP();
        callRemoteConfig();
    }

    private void startThreadToCancelApi() {
        Log.e("call_api_check", "cancelCallApi: false - " + System.currentTimeMillis());
        threadCallApi = new Thread(() -> {
            try {
                Thread.sleep(10000);
                cancelCallApi = true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadCallApi.start();
    }


    private void addAdsInGradle() {
        if (ConstantIdAds.listIDAdsOpenSplash.isEmpty()) {
            ConstantIdAds.listIDAdsOpenSplash.add(BuildConfig.open_splash);
        }

        if (ConstantIdAds.listIDAdsInterSplash.isEmpty()) {
            ConstantIdAds.listIDAdsInterSplash.add(BuildConfig.inter_splash);
        }

        if (ConstantIdAds.listIDAdsBannerSplash.isEmpty()) {
            ConstantIdAds.listIDAdsBannerSplash.add(BuildConfig.banner_splash);
        }

        if (ConstantIdAds.listIDAdsNativeLanguage.isEmpty()) {
            ConstantIdAds.listIDAdsNativeLanguage.add(BuildConfig.native_language);
        }

        if (ConstantIdAds.listIDAdsNativeIntro.isEmpty()) {
            ConstantIdAds.listIDAdsNativeIntro.add(BuildConfig.native_intro);
        }

        if (ConstantIdAds.listIDAdsNativeIntroFull.isEmpty()) {
            ConstantIdAds.listIDAdsNativeIntroFull.add(BuildConfig.native_intro_full);
        }

        if (ConstantIdAds.listIDAdsInterIntro.isEmpty()) {
            ConstantIdAds.listIDAdsInterIntro.add(BuildConfig.inter_intro);
        }

        if (ConstantIdAds.listIDAdsNativePermission.isEmpty()) {
            ConstantIdAds.listIDAdsNativePermission.add(BuildConfig.native_permission);
        }

        if (ConstantIdAds.listIDAdsNativeResume.isEmpty()) {
            ConstantIdAds.listIDAdsNativeResume.add(BuildConfig.native_resume);
        }

        if (ConstantIdAds.listIDAdsBannerAll.isEmpty()) {
            ConstantIdAds.listIDAdsBannerAll.add(BuildConfig.banner_all);
        }

        if (ConstantIdAds.listIDAdsNativePopup.isEmpty()) {
            ConstantIdAds.listIDAdsNativePopup.add(BuildConfig.native_popup);
        }

        if (ConstantIdAds.listIDAdsCollapseHome.isEmpty()) {
            ConstantIdAds.listIDAdsCollapseHome.add(BuildConfig.collapse_home);
        }

        if (ConstantIdAds.listIDAdsInterHome.isEmpty()) {
            ConstantIdAds.listIDAdsInterHome.add(BuildConfig.inter_home);
        }

        if (ConstantIdAds.listIDAdsNativeHome.isEmpty()) {
            ConstantIdAds.listIDAdsNativeHome.add(BuildConfig.native_home);
        }

        if (ConstantIdAds.listIDAdsNativeMenu.isEmpty()) {
            ConstantIdAds.listIDAdsNativeMenu.add(BuildConfig.native_menu);
        }

        if (ConstantIdAds.listIDAdsInterMenu.isEmpty()) {
            ConstantIdAds.listIDAdsInterMenu.add(BuildConfig.inter_menu);
        }

        if (ConstantIdAds.listIDAdsNativeFloating.isEmpty()) {
            ConstantIdAds.listIDAdsNativeFloating.add(BuildConfig.native_floating);
        }

        if (ConstantIdAds.listIDAdsInterFloating.isEmpty()) {
            ConstantIdAds.listIDAdsInterFloating.add(BuildConfig.inter_floating);
        }

        if (ConstantIdAds.listIDAdsNativeVolume.isEmpty()) {
            ConstantIdAds.listIDAdsNativeVolume.add(BuildConfig.native_volume);
        }

        if (ConstantIdAds.listIDAdsNativeButton.isEmpty()) {
            ConstantIdAds.listIDAdsNativeButton.add(BuildConfig.native_button);
        }

        if (ConstantIdAds.listIDAdsInterButton.isEmpty()) {
            ConstantIdAds.listIDAdsInterButton.add(BuildConfig.inter_button);
        }

    }

    private void loadBanner() {
        if (IsNetWork.haveNetworkConnectionUMP(SplashActivity.this) && !ConstantIdAds.listIDAdsBannerSplash.isEmpty() && ConstantRemote.banner_splash) {
            binding.rlBanner.setVisibility(View.VISIBLE);
            BannerCallback bannerCallback = new BannerCallback() {
                @Override
                public void onCheckComplete() {
                    super.onCheckComplete();
                    if (isShowAppOpenOrInter()) {
                        showAppOpenSplash();
                    } else {
                        showAppInterSplash();
                    }
                }
            };
            Admob.getInstance().loadBannerSplash(this, ConstantIdAds.listIDAdsBannerSplash, ConstantIdAds.listDriveID, bannerCallback, 2000);
        } else {
            binding.rlBanner.setVisibility(View.GONE);
            CheckAds.getInstance().init(this, ConstantIdAds.listDriveID, true);
            new Handler(getMainLooper()).postDelayed(() -> {
                if (isShowAppOpenOrInter()) {
                    showAppOpenSplash();
                } else {
                    showAppInterSplash();
                }
            }, 1500);
        }
    }

    private void clearAllAds() {
        ConstantIdAds.listIDAdsOpenSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsOpenSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsInterSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeLanguage = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeIntroFull = new ArrayList<>();
        ConstantIdAds.listIDAdsInterIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsNativePermission = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeResume = new ArrayList<>();
        ConstantIdAds.listIDAdsBannerAll = new ArrayList<>();
        ConstantIdAds.listIDAdsNativePopup = new ArrayList<>();
        ConstantIdAds.listIDAdsCollapseHome = new ArrayList<>();
        ConstantIdAds.listIDAdsInterHome = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeHome = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeMenu = new ArrayList<>();
        ConstantIdAds.listIDAdsInterMenu = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeFloating = new ArrayList<>();
        ConstantIdAds.listIDAdsInterFloating = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeVolume = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeButton = new ArrayList<>();
        ConstantIdAds.listIDAdsInterButton = new ArrayList<>();
        ConstantIdAds.listDriveID = new ArrayList<>();
    }

    private void callUMP() {

        if (IsNetWork.haveNetworkConnection(this)) {
            if (ConstantRemote.show_ump) {
                switch (SharePrefUtils.getInt(SPUtils.CONSENT_CHECK, 0)) {
                    case 0:
                        callConsent();
                        break;
                    case 1:
                        Application application = getApplication();
                        ((MyApplication) application).initAds();
                        new Handler().postDelayed(this::callApi, 1500);
                        break;
                    case 2:
                        new Handler().postDelayed(this::startNextActivity, 3000);
                        break;
                }
            } else {
                Application application = getApplication();
                ((MyApplication) application).initAds();
                new Handler().postDelayed(this::callApi, 1500);
            }
        } else {
            new Handler().postDelayed(this::startNextActivity, 3000);
        }

    }

    private void callRemoteConfig() {
        FirebaseApp.initializeApp(this);
        ConstantRemote.initRemoteConfig(task -> {
            if (task.isSuccessful()) {
                ConstantRemote.banner_splash = ConstantRemote.getRemoteConfigBoolean("banner_splash");
                ConstantRemote.open_splash = ConstantRemote.getRemoteConfigBoolean("open_splash");
                ConstantRemote.inter_splash = ConstantRemote.getRemoteConfigBoolean("inter_splash");
                ConstantRemote.native_language = ConstantRemote.getRemoteConfigBoolean("native_language");
                ConstantRemote.native_intro = ConstantRemote.getRemoteConfigBoolean("native_intro");
                ConstantRemote.native_intro_full = ConstantRemote.getRemoteConfigBoolean("native_intro_full");
                ConstantRemote.inter_intro = ConstantRemote.getRemoteConfigBoolean("inter_intro");
                ConstantRemote.native_permission = ConstantRemote.getRemoteConfigBoolean("native_permission");
                ConstantRemote.resume = ConstantRemote.getRemoteConfigBoolean("appopen_resume");
                ConstantRemote.native_resume = ConstantRemote.getRemoteConfigBoolean("native_resume");
                ConstantRemote.banner_all = ConstantRemote.getRemoteConfigBoolean("banner_all");
                ConstantRemote.native_popup = ConstantRemote.getRemoteConfigBoolean("native_popup");
                ConstantRemote.collapse_home = ConstantRemote.getRemoteConfigBoolean("collapse_home");
                ConstantRemote.inter_home = ConstantRemote.getRemoteConfigBoolean("inter_home");
                ConstantRemote.native_home = ConstantRemote.getRemoteConfigBoolean("native_home");
                ConstantRemote.native_menu = ConstantRemote.getRemoteConfigBoolean("native_menu");
                ConstantRemote.inter_menu = ConstantRemote.getRemoteConfigBoolean("inter_menu");
                ConstantRemote.native_floating = ConstantRemote.getRemoteConfigBoolean("native_floating");
                ConstantRemote.inter_floating = ConstantRemote.getRemoteConfigBoolean("inter_floating");
                ConstantRemote.native_volume = ConstantRemote.getRemoteConfigBoolean("native_volume");
                ConstantRemote.native_button = ConstantRemote.getRemoteConfigBoolean("native_button");
                ConstantRemote.inter_button = ConstantRemote.getRemoteConfigBoolean("inter_button");
                ConstantRemote.show_ads = ConstantRemote.getRemoteConfigBoolean("show_ads");

                ConstantRemote.rate_aoa_inter_splash = ConstantRemote.getRemoteConfigOpenSplash("rate_aoa_inter_splash");
                ConstantRemote.time_native_reload = ConstantRemote.getRemoteConfigLong("time_native_reload");
                ConstantRemote.collap_reload_interval = ConstantRemote.getRemoteConfigLong("collap_reload_interval");
                ConstantRemote.interval_interstitial_from_start = ConstantRemote.getRemoteConfigLong("interval_interstitial_from_start");
                ConstantRemote.interval_between_interstitial = ConstantRemote.getRemoteConfigLong("interval_between_interstitial");

                ConstantRemote.interval_interstitial_from_start_old = System.currentTimeMillis();

                Log.e("call_api", "banner_splash: " + ConstantRemote.banner_splash);
                Log.e("call_api", "open_splash: " + ConstantRemote.open_splash);
                Log.e("call_api", "inter_splash: " + ConstantRemote.inter_splash);
                Log.e("call_api", "native_language: " + ConstantRemote.native_language);
                Log.e("call_api", "native_intro: " + ConstantRemote.native_intro);
                Log.e("call_api", "native_intro_full: " + ConstantRemote.native_intro_full);
                Log.e("call_api", "inter_intro: " + ConstantRemote.inter_intro);
                Log.e("call_api", "native_permission: " + ConstantRemote.native_permission);
                Log.e("call_api", "appopen_resume: " + ConstantRemote.resume);
                Log.e("call_api", "native_resume: " + ConstantRemote.native_resume);
                Log.e("call_api", "banner_all: " + ConstantRemote.banner_all);
                Log.e("call_api", "native_popup: " + ConstantRemote.native_popup);
                Log.e("call_api", "collapse_home: " + ConstantRemote.collapse_home);
                Log.e("call_api", "inter_home: " + ConstantRemote.inter_home);
                Log.e("call_api", "native_home: " + ConstantRemote.native_home);
                Log.e("call_api", "native_menu: " + ConstantRemote.native_menu);
                Log.e("call_api", "inter_menu: " + ConstantRemote.inter_menu);
                Log.e("call_api", "native_floating: " + ConstantRemote.native_floating);
                Log.e("call_api", "inter_floating: " + ConstantRemote.inter_floating);
                Log.e("call_api", "native_volume: " + ConstantRemote.native_volume);
                Log.e("call_api", "native_button: " + ConstantRemote.native_button);
                Log.e("call_api", "inter_button: " + ConstantRemote.inter_button);
                Log.e("call_api", "show_ads: " + ConstantRemote.show_ads);

                Log.e("call_api", "rate_aoa_inter_splash: " + ConstantRemote.rate_aoa_inter_splash);
                Log.e("call_api", "time_native_reload: " + ConstantRemote.time_native_reload);
                Log.e("call_api", "collap_reload_interval: " + ConstantRemote.collap_reload_interval);
                Log.e("call_api", "interstitial_from_start: " + ConstantRemote.interval_interstitial_from_start);
                Log.e("call_api", "interval_between_interstitial: " + ConstantRemote.interval_between_interstitial);
            }
        });

    }

    private void callConsent() {
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        googleMobileAdsConsentManager.setSetTagForUnderAge(false);
        googleMobileAdsConsentManager.gatherConsent(this, complete -> {
            if (complete && googleMobileAdsConsentManager.canRequestAds()) {
                Application application = getApplication();
                ((MyApplication) application).initAds();
            }

            if (getConsentResult(this)) {
                SharePrefUtils.putInt(SPUtils.CONSENT_CHECK, 1);
            } else {
                SharePrefUtils.putInt(SPUtils.CONSENT_CHECK, 2);
            }

            if (getConsentResult(this) && googleMobileAdsConsentManager.canRequestAds()) {
                callApi();
            } else {
                new Handler().postDelayed(this::startNextActivity, 1500);
            }
        });
    }

    private void callApi() {
        if (IsNetWork.haveNetworkConnectionUMP(this)) {
            try {
                ApiService.apiService.callAdsSplash().enqueue(new Callback<List<AdsModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AdsModel>> call, @NonNull Response<List<AdsModel>> response) {
                        if (response.body() != null) {
                            List<AdsModel> adsModelList = response.body();
                            if (!adsModelList.isEmpty()) {
                                for (AdsModel ads : response.body()) {
                                    if (cancelCallApi) break;
                                    switch (ads.getName()) {
                                        case "banner_splash":
                                            ConstantIdAds.listIDAdsBannerSplash.add(ads.getAds_id());
                                            break;
                                        case "open_splash":
                                            ConstantIdAds.listIDAdsOpenSplash.add(ads.getAds_id());
                                            break;
                                        case "inter_splash":
                                            ConstantIdAds.listIDAdsInterSplash.add(ads.getAds_id());
                                            break;
                                        case "native_language":
                                            ConstantIdAds.listIDAdsNativeLanguage.add(ads.getAds_id());
                                            break;
                                        case "native_intro":
                                            ConstantIdAds.listIDAdsNativeIntro.add(ads.getAds_id());
                                            break;
                                        case "native_intro_full":
                                            ConstantIdAds.listIDAdsNativeIntroFull.add(ads.getAds_id());
                                            break;
                                        case "inter_intro":
                                            ConstantIdAds.listIDAdsInterIntro.add(ads.getAds_id());
                                            break;
                                        case "native_permission":
                                            ConstantIdAds.listIDAdsNativePermission.add(ads.getAds_id());
                                            break;
                                        case "native_resume":
                                            ConstantIdAds.listIDAdsNativeResume.add(ads.getAds_id());
                                            break;
                                        case "banner_all":
                                            ConstantIdAds.listIDAdsBannerAll.add(ads.getAds_id());
                                            break;
                                        case "native_popup":
                                            ConstantIdAds.listIDAdsNativePopup.add(ads.getAds_id());
                                            break;
                                        case "collapse_home":
                                            ConstantIdAds.listIDAdsCollapseHome.add(ads.getAds_id());
                                            break;
                                        case "inter_home":
                                            ConstantIdAds.listIDAdsInterHome.add(ads.getAds_id());
                                            break;
                                        case "native_home":
                                            ConstantIdAds.listIDAdsNativeHome.add(ads.getAds_id());
                                            break;
                                        case "native_menu":
                                            ConstantIdAds.listIDAdsNativeMenu.add(ads.getAds_id());
                                            break;
                                        case "inter_menu":
                                            ConstantIdAds.listIDAdsInterMenu.add(ads.getAds_id());
                                            break;
                                        case "native_floating":
                                            ConstantIdAds.listIDAdsNativeFloating.add(ads.getAds_id());
                                            break;
                                        case "inter_floating":
                                            ConstantIdAds.listIDAdsInterFloating.add(ads.getAds_id());
                                            break;
                                        case "native_volume":
                                            ConstantIdAds.listIDAdsNativeVolume.add(ads.getAds_id());
                                            break;
                                        case "native_button":
                                            ConstantIdAds.listIDAdsNativeButton.add(ads.getAds_id());
                                            break;
                                        case "inter_button":
                                            ConstantIdAds.listIDAdsInterButton.add(ads.getAds_id());
                                            break;
                                        case "drive_id_test":
                                            ConstantIdAds.listDriveID.add(ads.getAds_id());
                                            break;
                                    }
                                }
                                addAdsInGradle();
//                                ConstantIdAds.listDriveID.add("1a2a02d280ce6de3");
//                                ConstantIdAds.listDriveID.add("5a3487bea704af04");
                                loadBanner();

                                Log.e("call_api", "banner_splash: " + ConstantIdAds.listIDAdsBannerSplash.toString());
                                Log.e("call_api", "open_splash: " + ConstantIdAds.listIDAdsOpenSplash.toString());
                                Log.e("call_api", "inter_splash: " + ConstantIdAds.listIDAdsInterSplash.toString());
                                Log.e("call_api", "native_language: " + ConstantIdAds.listIDAdsNativeLanguage.toString());
                                Log.e("call_api", "native_intro: " + ConstantIdAds.listIDAdsNativeIntro.toString());
                                Log.e("call_api", "native_intro_full: " + ConstantIdAds.listIDAdsNativeIntroFull.toString());
                                Log.e("call_api", "inter_intro: " + ConstantIdAds.listIDAdsInterIntro.toString());
                                Log.e("call_api", "native_permission: " + ConstantIdAds.listIDAdsNativePermission.toString());
                                Log.e("call_api", "native_resume: " + ConstantIdAds.listIDAdsNativeResume.toString());
                                Log.e("call_api", "banner_all: " + ConstantIdAds.listIDAdsBannerAll.toString());
                                Log.e("call_api", "native_popup: " + ConstantIdAds.listIDAdsNativePopup.toString());
                                Log.e("call_api", "collapse_home: " + ConstantIdAds.listIDAdsCollapseHome.toString());
                                Log.e("call_api", "inter_home: " + ConstantIdAds.listIDAdsInterHome.toString());
                                Log.e("call_api", "native_home: " + ConstantIdAds.listIDAdsNativeHome.toString());
                                Log.e("call_api", "native_menu: " + ConstantIdAds.listIDAdsNativeMenu.toString());
                                Log.e("call_api", "inter_menu: " + ConstantIdAds.listIDAdsInterMenu.toString());
                                Log.e("call_api", "native_floating: " + ConstantIdAds.listIDAdsNativeFloating.toString());
                                Log.e("call_api", "inter_floating: " + ConstantIdAds.listIDAdsInterFloating.toString());
                                Log.e("call_api", "native_volume: " + ConstantIdAds.listIDAdsNativeVolume.toString());
                                Log.e("call_api", "native_button: " + ConstantIdAds.listIDAdsNativeButton.toString());
                                Log.e("call_api", "inter_button: " + ConstantIdAds.listIDAdsInterButton.toString());

                                Log.e("call_api", "drive_id_test: " + ConstantIdAds.listDriveID.toString());

                            } else {
                                new Handler().postDelayed(() -> startNextActivity(), 3000);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AdsModel>> call, @NonNull Throwable t) {
                        Log.e("Kien", "error");
                        new Handler().postDelayed(() -> startNextActivity(), 3000);
                    }
                });
            } catch (Exception e) {
                binding.rlBanner.setVisibility(View.GONE);
                new Handler().postDelayed(this::startNextActivity, 3000);
            }
        } else {
            binding.rlBanner.setVisibility(View.GONE);
            new Handler().postDelayed(this::startNextActivity, 3000);
        }
    }

    private boolean isShowAppOpenOrInter() {
        try {
            int appOpenRate = Integer.parseInt(ConstantRemote.rate_aoa_inter_splash.get(0));
            int random = new Random().nextInt(99) + 1;
            return random <= appOpenRate;
        } catch (Exception e) {
            return true;
        }
    }

    private void showAppOpenSplash() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsOpenSplash.isEmpty() && ConstantRemote.open_splash) {
            adCallback = new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    startNextActivity();
                }
            };

            AppOpenManager.getInstance().loadOpenAppAdSplashFloor(SplashActivity.this, ConstantIdAds.listIDAdsOpenSplash, true, adCallback);
        } else {
            startNextActivity();
        }
    }

    private void showAppInterSplash() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterSplash.isEmpty() && ConstantRemote.inter_splash) {
            CommonAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, ConstantIdAds.listIDAdsInterSplash, 15000, 3500, new CommonAdCallback() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    startNextActivity();
                }

                @Override
                public void onAdFailedToLoad(ApAdError adError) {
                    super.onAdFailedToLoad(adError);
                    startNextActivity();
                }

                @Override
                public void onAdFailedToShow(ApAdError adError) {
                    super.onAdFailedToShow(adError);
                    startNextActivity();
                }
            });
        } else {
            startNextActivity();
        }
    }

    public void startNextActivity() {
        if (ConstantRemote.resume) {
            AppOpenManager.getInstance().enableAppResume();
        } else {
            AppOpenManager.getInstance().disableAppResume();
        }
        startNextActivity(LanguageStartActivity.class, null);
        finishAffinity();
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {

    }
}
