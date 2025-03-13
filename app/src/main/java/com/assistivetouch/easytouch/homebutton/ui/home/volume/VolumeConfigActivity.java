package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityVolumeConfigBinding;
import com.assistivetouch.easytouch.homebutton.dialog.ChooseActionDialog;
import com.assistivetouch.easytouch.homebutton.dialog.GoToSettingDialog;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

public class VolumeConfigActivity extends BaseActivity<ActivityVolumeConfigBinding> {

    ChooseActionDialog dialog;

    @Override
    public ActivityVolumeConfigBinding getBinding() {
        return ActivityVolumeConfigBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        changeState();
        switch (SPUtils.getInt(getBaseContext(), SPUtils.LONG_PRESS_VOLUME_ACTION, 1)) {
            case 1:
                binding.tvAction.setText(R.string.hide_button);
                break;
            case 2:
                binding.tvAction.setText(R.string.screen_off);
                break;
            case 3:
                binding.tvAction.setText(R.string.open_notification);
                break;
            case 4:
                binding.tvAction.setText(R.string.mute_media_volume);
                break;
        }
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.llHideButton.setOnClickListener(v -> {
            showChooseActionDialog();
        });
        binding.clMedia.setOnClickListener(v -> {
            SPUtils.setBoolean(this, SPUtils.SHOW_MEDIA, !SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true));
            changeState();
        });
        binding.clRing.setOnClickListener(v -> {
            SPUtils.setBoolean(this, SPUtils.SHOW_RINGTONE, !SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true));
            changeState();
        });
        binding.clNotification.setOnClickListener(v -> {
            SPUtils.setBoolean(this, SPUtils.SHOW_NOTIFICATION, !SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true));
            changeState();
        });
        binding.clCall.setOnClickListener(v -> {
            SPUtils.setBoolean(this, SPUtils.SHOW_CALL, !SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true));
            changeState();
        });
        binding.clBrightness.setOnClickListener(v -> {
            if (!CheckUtils.checkSystemWriteSetting(this)) {
                showDialogGotoSetting(3);
            } else {
                SPUtils.setBoolean(this, SPUtils.SHOW_BRIGHTNESS, !SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, true));
                changeState();
            }
        });
        binding.clDark.setOnClickListener(v -> {
            SPUtils.setBoolean(this, SPUtils.SHOW_DARKNESS, !SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, true));
            changeState();
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void changeState() {
        if (SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true)) {
            binding.clMedia.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt1.setVisibility(VISIBLE);
        } else {
            binding.clMedia.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt1.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true)) {
            binding.clRing.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt2.setVisibility(VISIBLE);
        } else {
            binding.clRing.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt2.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true)) {
            binding.clNotification.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt3.setVisibility(VISIBLE);
        } else {
            binding.clNotification.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt3.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true)) {
            binding.clCall.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt4.setVisibility(VISIBLE);
        } else {
            binding.clCall.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt4.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false)) {
            binding.clBrightness.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt5.setVisibility(VISIBLE);
        } else {
            binding.clBrightness.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt5.setVisibility(INVISIBLE);
        }
        if (SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false)) {
            binding.clDark.setBackgroundResource(R.drawable.bg_volume_option_s);
            binding.ivt6.setVisibility(VISIBLE);
        } else {
            binding.clDark.setBackgroundResource(R.drawable.bg_lang_item_sn);
            binding.ivt6.setVisibility(INVISIBLE);
        }
    }

    private void showChooseActionDialog() {
        dialog = new ChooseActionDialog(this);
        int x = SPUtils.getInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 1);
        dialog.binding.llHideButton.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 1);
            changeStateAction(1);
        });
        dialog.binding.llScreenOff.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 2);
            changeStateAction(2);
        });
        dialog.binding.llOpenNotification.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 3);
            changeStateAction(3);
        });
        dialog.binding.llMuteMedia.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 4);
            changeStateAction(4);
        });
        dialog.show();
        changeStateAction(x);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                switch (SPUtils.getInt(getBaseContext(), SPUtils.LONG_PRESS_VOLUME_ACTION, 1)) {
                    case 1:
                        binding.tvAction.setText(R.string.hide_button);
                        break;
                    case 2:
                        binding.tvAction.setText(R.string.screen_off);
                        break;
                    case 3:
                        binding.tvAction.setText(R.string.open_notification);
                        break;
                    case 4:
                        binding.tvAction.setText(R.string.mute_media_volume);
                        break;
                }

            }
        });
    }

    private void changeStateAction(int i) {
        if (dialog != null && dialog.isShowing()) {
            resetChange();
            switch (i) {
                case 1:
                    dialog.binding.ivHideButton.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 2:
                    dialog.binding.ivScreenOff.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 3:
                    dialog.binding.ivOpenNotification.setImageResource(R.drawable.radio_volume_s);
                    break;
                case 4:
                    dialog.binding.ivMuteMedia.setImageResource(R.drawable.radio_volume_s);
                    break;
            }

        }

    }

    private void resetChange() {
        dialog.binding.ivHideButton.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivOpenNotification.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivScreenOff.setImageResource(R.drawable.radio_volume_sn);
        dialog.binding.ivMuteMedia.setImageResource(R.drawable.radio_volume_sn);
    }

    private void showDialogGotoSetting(int type) {
        GoToSettingDialog dialog = new GoToSettingDialog(this, true);
        SystemUtil.setLocale(this);

        if (type == 1) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_noti);
        } else if (type == 2) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_overlay);
        } else if (type == 3) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_write_setting);
        } else if (type == 4) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_accessibility);
        } else if (type == 5) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_camera);
        } else if (type == 6) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_storage);
        }

        dialog.binding.tvStay.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvContent.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvAgree.setOnClickListener(view -> {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(HomeActivity.class);
            dialog.dismiss();
            if (type == 1 || type == 5 || type == 6) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                resultLauncher.launch(intent);
            } else if (type == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        resultLauncher.launch(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PermissionError", "Error opening settings: " + e.getMessage());
                    }

                }
            } else if (type == 3) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else if (type == 4) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Log.e("check_service", "off");
            }
        });
        dialog.show();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK || result.getResultCode() == RESULT_CANCELED) {
            //ads
            Log.d("activity_check", "home");
        }
    });
}