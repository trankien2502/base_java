package com.livescore.soccerscore.matchlive.ui.livescores.live;

import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.databinding.ActivityLiveScoreBinding;

public class LiveScoreActivity extends BaseActivity<ActivityLiveScoreBinding> {


    @Override
    public ActivityLiveScoreBinding getBinding() {
        return ActivityLiveScoreBinding.inflate(getLayoutInflater());
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