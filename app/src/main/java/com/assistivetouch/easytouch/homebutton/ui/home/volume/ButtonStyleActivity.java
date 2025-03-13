package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class ButtonStyleActivity extends BaseActivity<ActivityButtonStyleBinding> {


    int currentButtonColor;
    int currentButtonBackgroundColor;

    @Override
    public ActivityButtonStyleBinding getBinding() {
        return ActivityButtonStyleBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

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
                if (ServiceScreen.instance != null && ServiceScreen.instance.volumeView != null) {
                    ServiceScreen.instance.volumeView.setAlpha((float) progress /255);
                }
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
                if (ServiceScreen.instance != null && ServiceScreen.instance.volumeView != null) {
                    ServiceScreen.instance.updateFloatingViewSize(progress);
                }
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

                binding.tvEdgeDistance.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.swPosition.setOnClickListener(v -> {

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
        ColorPickerDialog dialog = new ColorPickerDialog(this, true);
        dialog.init(new ColorSelectCallBack() {
            @Override
            public void select(int color) {
                if (isButtonColor) {
                    currentButtonColor = color;
                    binding.ivButtonColor.setCardBackgroundColor(color);
                } else {
                    currentButtonBackgroundColor = color;
                    binding.ivButtonBackgroundColor.setCardBackgroundColor(color);
                }
            }
        });
        dialog.show();
    }
}