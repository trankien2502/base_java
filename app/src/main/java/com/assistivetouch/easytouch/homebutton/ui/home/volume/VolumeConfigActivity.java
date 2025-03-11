package com.assistivetouch.easytouch.homebutton.ui.home.volume;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityVolumeConfigBinding;

public class VolumeConfigActivity extends BaseActivity<ActivityVolumeConfigBinding> {


    @Override
    public ActivityVolumeConfigBinding getBinding() {
        return ActivityVolumeConfigBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}