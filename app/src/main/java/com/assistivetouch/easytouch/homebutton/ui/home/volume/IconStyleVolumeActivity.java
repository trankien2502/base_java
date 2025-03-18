package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityIconStyleVolumeBinding;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

public class IconStyleVolumeActivity extends BaseActivity<ActivityIconStyleVolumeBinding> {


    int currentStyle = 1;
    @Override
    public ActivityIconStyleVolumeBinding getBinding() {
        return ActivityIconStyleVolumeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(getBaseContext(), "volume_config_icon_style_view");
        currentStyle = SPUtils.getInt(this,SPUtils.VOLUME_STYLE_NUMBER,1);
        changeState(false);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            EventTracking.logEvent(getBaseContext(), "volume_config_icon_style_view");
            SPUtils.setInt(this,SPUtils.VOLUME_STYLE_NUMBER,currentStyle);
            if (ServiceScreen.instance!=null){
                if (ServiceScreen.instance.volumeView !=null){
                    ServiceScreen.instance.removeVolumeView();
                    ServiceScreen.instance.addVolumeIcon();
                }
            }
            onBack();
        });
        binding.ivStyle1.setOnClickListener(v -> {
            currentStyle = 1;
            changeState(true);
        });
        binding.ivStyle2.setOnClickListener(v -> {
            currentStyle = 2;
            changeState(true);
        });
        binding.ivStyle3.setOnClickListener(v -> {
            currentStyle = 3;
            changeState(true);
        });
        binding.ivStyle4.setOnClickListener(v -> {
            currentStyle = 4;
            changeState(true);
        });
        binding.ivStyle5.setOnClickListener(v -> {
            currentStyle = 5;
            changeState(true);
        });
        binding.ivStyle6.setOnClickListener(v -> {
            currentStyle = 6;
            changeState(true);
        });
        binding.ivStyle7.setOnClickListener(v -> {
            currentStyle = 7;
            changeState(true);
        });
        binding.ivStyle8.setOnClickListener(v -> {
            currentStyle = 8;
            changeState(true);
        });
    }
    private void resetChange(){
        binding.ivStyle1.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle2.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle3.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle4.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle5.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle6.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle7.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
        binding.ivStyle8.setBackgroundResource(R.drawable.bg_icon_style_volume_n);
    }
    private void changeState(boolean isClick){
        if (isClick) EventTracking.logEvent(getBaseContext(), "volume_config_icon_style_item_click");
        resetChange();
        switch (currentStyle){
            case 2:
                binding.ivStyle2.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 3:
                binding.ivStyle3.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 4:
                binding.ivStyle4.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 5:
                binding.ivStyle5.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 6:
                binding.ivStyle6.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 7:
                binding.ivStyle7.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            case 8:
                binding.ivStyle8.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;
            default:
                binding.ivStyle1.setBackgroundResource(R.drawable.bg_icon_style_volume);
                break;

        }
    }
    @Override
    public void onBack() {
        EventTracking.logEvent(getBaseContext(), "volume_config_icon_style_view");
        setResult(RESULT_OK);
        finish();
    }
}