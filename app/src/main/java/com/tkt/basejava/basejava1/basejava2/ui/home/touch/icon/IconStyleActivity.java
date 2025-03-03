package com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityIconStyleBinding;

import java.util.ArrayList;
import java.util.List;

public class IconStyleActivity extends BaseActivity<ActivityIconStyleBinding> {

    IconStyleAdapter iconStyleAdapter;
    List<IconStyle> iconStyleList = new ArrayList<>();

    @Override
    public ActivityIconStyleBinding getBinding() {
        return ActivityIconStyleBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        iconStyleAdapter = new IconStyleAdapter(this, iconStyleList, new IconStyleCallBack() {
            @Override
            public void select(IconStyle iconStyle) {

            }
        });
        binding.rcvIcon.setAdapter(iconStyleAdapter);
    }

    private void initData() {
        iconStyleList.add(new IconStyle(R.drawable.icon_1,true));
        iconStyleList.add(new IconStyle(R.drawable.icon_2));
        iconStyleList.add(new IconStyle(R.drawable.icon_3));
        iconStyleList.add(new IconStyle(R.drawable.icon_4));
        iconStyleList.add(new IconStyle(R.drawable.icon_5));
        iconStyleList.add(new IconStyle(R.drawable.icon_6));
        iconStyleList.add(new IconStyle(R.drawable.icon_7));
        iconStyleList.add(new IconStyle(R.drawable.icon_8));
        iconStyleList.add(new IconStyle(R.drawable.icon_9));
        iconStyleList.add(new IconStyle(R.drawable.icon_10));
        iconStyleList.add(new IconStyle(R.drawable.icon_11));
        iconStyleList.add(new IconStyle(R.drawable.icon_12));
        iconStyleList.add(new IconStyle(R.drawable.icon_13));
        iconStyleList.add(new IconStyle(R.drawable.icon_14));
        iconStyleList.add(new IconStyle(R.drawable.icon_15));
        iconStyleList.add(new IconStyle(R.drawable.icon_16));
        iconStyleList.add(new IconStyle(R.drawable.icon_17));
        iconStyleList.add(new IconStyle(R.drawable.icon_18));
        iconStyleList.add(new IconStyle(R.drawable.icon_19));
        iconStyleList.add(new IconStyle(R.drawable.icon_20));
        iconStyleList.add(new IconStyle(R.drawable.icon_21));
        iconStyleList.add(new IconStyle(R.drawable.icon_22));
        iconStyleList.add(new IconStyle(R.drawable.icon_23));
        iconStyleList.add(new IconStyle(R.drawable.icon_24));

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