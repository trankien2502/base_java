package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityIconStyleBinding;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class IconStyleActivity extends BaseActivity<ActivityIconStyleBinding> {

    IconStyleAdapter iconStyleAdapter;
    List<IconStyle> iconStyleList = new ArrayList<>();
    int oldIconSource, currentIconSource;

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
                if (ServiceScreen.instance != null)
                    ServiceScreen.instance.setIconStyle(iconStyle.getSource());
                currentIconSource = iconStyle.getSource();
            }
        });
        binding.rcvIcon.setAdapter(iconStyleAdapter);
    }

    private void initData() {
        iconStyleList.add(new IconStyle(R.drawable.icon_1));
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
        oldIconSource = SPUtils.getInt(this, SPUtils.ICON_STYLE, R.drawable.icon_1);
        for (IconStyle iconStyle : iconStyleList)
            iconStyle.setSelect(iconStyle.getSource() == oldIconSource);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            if (ServiceScreen.instance != null)
                ServiceScreen.instance.setIconStyle(oldIconSource);
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            SPUtils.setInt(this, SPUtils.ICON_STYLE, currentIconSource);
            onBack();
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

}