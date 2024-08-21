package com.tkt.storage_manager.ui.policy;

import android.annotation.SuppressLint;
import android.view.View;

import com.tkt.storage_manager.ads.IsNetWork;
import com.tkt.storage_manager.base.BaseActivity;
import com.tkt.storage_manager.R;
import com.tkt.storage_manager.databinding.ActivityPolicyBinding;

public class PolicyActivity extends BaseActivity<ActivityPolicyBinding> {

    String linkPolicy = "";

    @Override
    public ActivityPolicyBinding getBinding() {
        return ActivityPolicyBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        binding.tvTitle.setText(getString(R.string.privacy_policy));

        if (!linkPolicy.isEmpty() && IsNetWork.haveNetworkConnection(this)) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.lnNoInternet.setVisibility(View.GONE);

            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.loadUrl(linkPolicy);
        } else {
            binding.webView.setVisibility(View.GONE);
            binding.lnNoInternet.setVisibility(View.VISIBLE);
        }

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(linkPolicy);
    }

    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(v -> onBackPressed());
    }

}
