package com.tkt.spin_wheel.ui.setting;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.databinding.ActivityAboutBinding;
import com.tkt.spin_wheel.ui.policy.PolicyActivity;
import com.tkt.spin_wheel.util.EventTracking;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(AboutActivity.this, "view_about_app");
        String text = getString(R.string.privacy_policy);
        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int color = getResources().getColor(R.color.main);// Đặt màu mong muốn ở đây
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.privacy.setText(spannableString);
    }

    @Override
    public void bindView() {
        binding.privacy.setOnClickListener(view -> {
            startNextActivity(PolicyActivity.class, null);
        });
        binding.ivGone.setOnClickListener(view -> onBackPressed());
    }
}