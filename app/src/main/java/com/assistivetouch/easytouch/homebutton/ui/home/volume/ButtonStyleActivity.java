package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import com.assistivetouch.easytouch.homebutton.databinding.ActivityButtonStyleBinding;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorPickerDialog;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorSelectCallBack;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.Menu1Fragment;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.Menu2Fragment;
import com.assistivetouch.easytouch.homebutton.ui.setting.SettingActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class ButtonStyleActivity extends BaseActivity<ActivityButtonStyleBinding> {


    int currentButtonColor;
    int currentButtonBackgroundColor;
    Handler handler = new Handler();
    Runnable runnableNativeAds;
    Runnable runnableNativeDialogAds;
    ColorPickerDialog colorDialog;

    @Override
    public ActivityButtonStyleBinding getBinding() {
        return ActivityButtonStyleBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        loadNativeButtonAds();
        EventTracking.logEvent(getBaseContext(), "volume_config_button_style_view");
        if (SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_COLOR, -1) != -1)
            binding.ivButtonColor.setBackgroundColor(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_COLOR, -1));
        if (SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, -1) != -1)
            binding.ivButtonBackgroundColor.setBackgroundColor(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, -1));
        binding.sbTransparency.setProgress(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_ALPHA, 128));
        binding.tvPercentTransparency.setText(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_ALPHA, 128) * 100 / 255 + "%");
        binding.sbSize.setProgress(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_SIZE, 0));
        binding.tvPercentSize.setText(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_SIZE, 0) * 100 / 60 + "%");
        binding.sbEdgeDistance.setProgress(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0));
        binding.tvEdgeDistance.setText(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) + "%");
        binding.swPosition.setChecked(SPUtils.getBoolean(this, SPUtils.VOLUME_BUTTON_FIX_POSITION, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.clIconStyle.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_button_style_icon_click");
            showInterButton(new Intent(this, IconStyleVolumeActivity.class));
        });
        binding.clButtonColor.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_button_style_color_click");
            showColorPickerDialog(true);
        });
        binding.clButtonBackgroundColor.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_button_style_background_color_click");
            showColorPickerDialog(false);
        });
        binding.sbTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ServiceScreen.instance != null) {
                    if (ServiceScreen.instance.volumeView != null) {
                        ServiceScreen.instance.volumeView.setAlpha((float) progress / 255);
                    }
                }

                SPUtils.setInt(getBaseContext(), SPUtils.VOLUME_BUTTON_ALPHA, progress);
                binding.tvPercentTransparency.setText(progress * 100 / 255 + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(), "volume_config_button_style_transparency_click");
            }
        });
        binding.sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ServiceScreen.instance != null) {
                    if (ServiceScreen.instance.volumeView != null) {
                        ServiceScreen.instance.updateFloatingViewSize(progress);
                    }
                }
                SPUtils.setInt(getBaseContext(), SPUtils.VOLUME_BUTTON_SIZE, progress);
                binding.tvPercentSize.setText(progress * 100 / 60 + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(), "volume_config_button_style_size_click");
            }
        });
        binding.sbEdgeDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SPUtils.setInt(getBaseContext(), SPUtils.VOLUME_BUTTON_DISTANCE, progress);
                binding.tvEdgeDistance.setText(progress + "%");
                if (ServiceScreen.instance != null) {
                    if (ServiceScreen.instance.volumeView != null) {
                        ServiceScreen.instance.updatePositionAfterMoveVolume();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventTracking.logEvent(getBaseContext(), "volume_config_button_style_distance_click");
            }
        });
        binding.swPosition.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_button_style_enable_position_click");
            SPUtils.setBoolean(getBaseContext(), SPUtils.VOLUME_BUTTON_FIX_POSITION, binding.swPosition.isChecked());
        });
    }

    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "volume_config_button_style_back_click");
        setResult(RESULT_OK);
        finish();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        isResume = false;
        if (result.getResultCode() == RESULT_OK) {
            loadNativeButtonAds();
            Log.d("activity_check", "home");
        }
    });

    private void showColorPickerDialog(boolean isButtonColor) {
        int color = isButtonColor ? SPUtils.getInt(getBaseContext(), SPUtils.VOLUME_BUTTON_COLOR, Color.WHITE) : SPUtils.getInt(getBaseContext(), SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, Color.WHITE);
        colorDialog = new ColorPickerDialog(this, true, color);
        colorDialog.init(new ColorSelectCallBack() {
            @Override
            public void select(int color) {
                if (isButtonColor) {
                    currentButtonColor = color;
                    SPUtils.setInt(getBaseContext(), SPUtils.VOLUME_BUTTON_COLOR, color);
                    binding.ivButtonColor.setBackgroundColor(color);
                } else {
                    currentButtonBackgroundColor = color;
                    SPUtils.setInt(getBaseContext(), SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, color);
                    binding.ivButtonBackgroundColor.setBackgroundColor(color);
                }
                if (ServiceScreen.instance != null) {
                    if (ServiceScreen.instance.volumeView != null) {
                        ServiceScreen.instance.removeVolumeView();
                        ServiceScreen.instance.addVolumeIcon();
                    }
                }
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

    public void loadNativeButtonAds() {
        try {
            if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsNativeButton.isEmpty() && ConstantRemote.native_button && ConstantRemote.show_ads) {
                handler.removeCallbacks(runnableNativeAds);
                runnableNativeAds = new Runnable() {
                    @Override
                    public void run() {
                        loadNativeButtonAds();
                    }
                };
                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.layout_native_load_large_cta_above, null);
                binding.nativeButton.removeAllViews();
                binding.nativeButton.addView(adViewLoad);
                binding.nativeButton.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeButton, new AdCallback() {
                        @Override
                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                            runOnUiThread(() -> {
                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_native_show_large_cta_above, null);
                                binding.nativeButton.removeAllViews();
                                binding.nativeButton.addView(adView);
                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                if (ConstantRemote.time_native_reload != 0)
                                    handler.postDelayed(runnableNativeAds, ConstantRemote.time_native_reload * 1000);
                                CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@org.jetbrains.annotations.Nullable LoadAdError i) {
                            runOnUiThread(() -> {
                                binding.nativeButton.setVisibility(View.GONE);
                            });
                        }
                    });
                }).start();

            } else {
                binding.nativeButton.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            binding.nativeButton.setVisibility(View.GONE);
        }
    }

    private void loadInterButton() {
        if (ConstantIdAds.mInterButton == null && IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterButton.isEmpty() && ConstantRemote.inter_button && ConstantRemote.show_ads) {
            ConstantIdAds.mInterButton = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterButton);
        }
    }

    private void showInterButton(Intent intent) {
        if (IsNetWork.haveNetworkConnectionUMP(this) && !ConstantIdAds.listIDAdsInterButton.isEmpty() && ConstantRemote.inter_button && ConstantRemote.show_ads) {
            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start_old > ConstantRemote.interval_interstitial_from_start * 1000) {
                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
                    try {
                        if (ConstantIdAds.mInterButton != null) {
                            CommonAd.getInstance().forceShowInterstitialByTime(this, ConstantIdAds.mInterButton, new CommonAdCallback() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    resultLauncher.launch(intent);
                                }

                                @Override
                                public void onAdClosedByTime() {
                                    super.onAdClosedByTime();
                                    ConstantIdAds.mInterButton = null;
                                    ConstantRemote.time_interval_old = System.currentTimeMillis();
                                    loadInterButton();
                                }
                            }, true);
                        } else {
                            loadInterButton();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableNativeAds);
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
}