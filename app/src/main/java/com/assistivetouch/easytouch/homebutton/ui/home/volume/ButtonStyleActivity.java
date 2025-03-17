package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityButtonStyleBinding;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorPickerDialog;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorSelectCallBack;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.Menu1Fragment;
import com.assistivetouch.easytouch.homebutton.ui.home.touch.custom.Menu2Fragment;
import com.assistivetouch.easytouch.homebutton.ui.setting.SettingActivity;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

public class ButtonStyleActivity extends BaseActivity<ActivityButtonStyleBinding> {


    int currentButtonColor;
    int currentButtonBackgroundColor;

    @Override
    public ActivityButtonStyleBinding getBinding() {
        return ActivityButtonStyleBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
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
            resultLauncher.launch(new Intent(this, IconStyleVolumeActivity.class));
        });
        binding.clButtonColor.setOnClickListener(v -> {
            showColorPickerDialog(true);
        });
        binding.clButtonBackgroundColor.setOnClickListener(v -> {
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

            }
        });
        binding.swPosition.setOnClickListener(v -> {
            SPUtils.setBoolean(getBaseContext(), SPUtils.VOLUME_BUTTON_FIX_POSITION, binding.swPosition.isChecked());
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    private void showColorPickerDialog(boolean isButtonColor) {
        int color = isButtonColor ? SPUtils.getInt(getBaseContext(), SPUtils.VOLUME_BUTTON_COLOR, Color.WHITE) : SPUtils.getInt(getBaseContext(), SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, Color.WHITE);
        ColorPickerDialog dialog = new ColorPickerDialog(this, true, color);
        dialog.init(new ColorSelectCallBack() {
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
        dialog.show();
    }
}