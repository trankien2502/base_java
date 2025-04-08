package com.livescore.soccerscore.matchlive.dialog.notification;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.L;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.base.BaseDialog;
import com.livescore.soccerscore.matchlive.databinding.DialogNotificationBinding;
import com.livescore.soccerscore.matchlive.databinding.DialogPermissionBinding;


public class NotificationDialog extends BaseDialog<DialogNotificationBinding> {

    FixtureModel fixtureModel;
    DialogNotificationCallBack callBack;

    public NotificationDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogNotificationBinding setBinding() {
        return DialogNotificationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.swNotification.setOnClickListener(v -> {
            if (!binding.swNotification.isChecked()) {
                fixtureModel.isAlarm = false;
                fixtureModel.start_match = false;
                fixtureModel.end_first_half = false;
                fixtureModel.start_second_half = false;
                fixtureModel.goals = false;
                fixtureModel.red_card = false;
                fixtureModel.end_match = false;
                fixtureModel.before_match = -1;
            } else {
                fixtureModel.isAlarm = true;
                fixtureModel.start_match = true;
                fixtureModel.end_first_half = true;
                fixtureModel.start_second_half = true;
                fixtureModel.goals = true;
                fixtureModel.red_card = true;
                fixtureModel.end_match = true;
                fixtureModel.before_match = 60;
            }
            switchAlarm(fixtureModel);
        });
        binding.ivStartMatch.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.start_match = !fixtureModel.start_match;
                switchAlarm(fixtureModel);
            }
        });
        binding.ivBeforeMatch.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                switchAlarm(fixtureModel);
            }
        });
        binding.ivEndFirstMatch.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.end_first_half = !fixtureModel.end_first_half;
                switchAlarm(fixtureModel);
            }
        });
        binding.ivStartSecondMatch.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.start_second_half = !fixtureModel.start_second_half;
                switchAlarm(fixtureModel);
            }
        });
        binding.ivGoal.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.goals = !fixtureModel.goals;
                switchAlarm(fixtureModel);
            }
        });
        binding.ivRedCard.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.red_card = !fixtureModel.red_card;
                switchAlarm(fixtureModel);
            }
        });
        binding.ivEndMatch.setOnClickListener(v -> {
            if (fixtureModel.isAlarm) {
                fixtureModel.end_match = !fixtureModel.end_match;
                switchAlarm(fixtureModel);
            }
        });
        binding.btnCancel.setOnClickListener(v -> callBack.cancel());
        binding.btnSave.setOnClickListener(v -> {
            int bm = binding.numberHour.getValue() * 60 + binding.numberMinute.getValue();
            if (bm == 0) {
                Toast.makeText(getContext(), R.string.please_choose_time_before_match_greater_than_0, Toast.LENGTH_SHORT).show();
            } else {
                fixtureModel.before_match = bm;
                callBack.save(fixtureModel);
            }

        });
    }

    public void initCallBack(DialogNotificationCallBack callBack) {
        this.callBack = callBack;
    }

    public void initFixture(FixtureModel fixtureModel) {
        this.fixtureModel = fixtureModel;
        switchAlarm(fixtureModel);
    }

    private void switchAlarm(FixtureModel fixtureModel) {
        Log.e("check_dialog", "fixture: " + fixtureModel);
        if (fixtureModel.isAlarm) {
            binding.swNotification.setChecked(true);
        } else {
            binding.swNotification.setChecked(false);
        }
        if (fixtureModel.before_match != -1) {
            binding.ivBeforeMatch.setImageResource(R.drawable.check_box_s);
            binding.tvBeforeMatch.setTextColor(Color.parseColor("#000000"));
            binding.tvHours.setTextColor(Color.parseColor("#000000"));
            binding.tvMinutes.setTextColor(Color.parseColor("#000000"));
            binding.numberHour.setTextColor(Color.parseColor("#000000"));
            binding.numberHour.setSelectedTextColor(Color.parseColor("#000000"));
            binding.numberMinute.setTextColor(Color.parseColor("#000000"));
            binding.numberMinute.setSelectedTextColor(Color.parseColor("#000000"));
            binding.numberHour.setValue(fixtureModel.before_match / 60);
            binding.numberMinute.setValue(fixtureModel.before_match % 60);
            binding.numberHour.setEnabled(true);
            binding.numberMinute.setEnabled(true);
        } else {
            binding.ivBeforeMatch.setImageResource(R.drawable.check_box_sn);
            binding.tvBeforeMatch.setTextColor(Color.parseColor("#809EB4"));
            binding.tvHours.setTextColor(Color.parseColor("#809EB4"));
            binding.tvMinutes.setTextColor(Color.parseColor("#809EB4"));
            binding.numberHour.setTextColor(Color.parseColor("#809EB4"));
            binding.numberHour.setSelectedTextColor(Color.parseColor("#809EB4"));
            binding.numberMinute.setTextColor(Color.parseColor("#809EB4"));
            binding.numberMinute.setSelectedTextColor(Color.parseColor("#809EB4"));
            binding.numberHour.setEnabled(false);
            binding.numberMinute.setEnabled(false);
        }
        if (fixtureModel.start_match) {
            binding.ivStartMatch.setImageResource(R.drawable.check_box_s);
            binding.tvStartMatch.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivStartMatch.setImageResource(R.drawable.check_box_sn);
            binding.tvStartMatch.setTextColor(Color.parseColor("#809EB4"));
        }
        if (fixtureModel.end_first_half) {
            binding.ivEndFirstMatch.setImageResource(R.drawable.check_box_s);
            binding.tvEndFirstMatch.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivEndFirstMatch.setImageResource(R.drawable.check_box_sn);
            binding.tvEndFirstMatch.setTextColor(Color.parseColor("#809EB4"));
        }
        if (fixtureModel.start_second_half) {
            binding.ivStartSecondMatch.setImageResource(R.drawable.check_box_s);
            binding.tvStartSecondMatch.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivStartSecondMatch.setImageResource(R.drawable.check_box_sn);
            binding.tvStartSecondMatch.setTextColor(Color.parseColor("#809EB4"));
        }
        if (fixtureModel.goals) {
            binding.ivGoal.setImageResource(R.drawable.check_box_s);
            binding.tvGoal.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivGoal.setImageResource(R.drawable.check_box_sn);
            binding.tvGoal.setTextColor(Color.parseColor("#809EB4"));
        }
        if (fixtureModel.red_card) {
            binding.ivRedCard.setImageResource(R.drawable.check_box_s);
            binding.tvRedCard.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivRedCard.setImageResource(R.drawable.check_box_sn);
            binding.tvRedCard.setTextColor(Color.parseColor("#809EB4"));
        }
        if (fixtureModel.end_match) {
            binding.ivEndMatch.setImageResource(R.drawable.check_box_s);
            binding.tvEndMatch.setTextColor(Color.parseColor("#000000"));
        } else {
            binding.ivEndMatch.setImageResource(R.drawable.check_box_sn);
            binding.tvEndMatch.setTextColor(Color.parseColor("#809EB4"));
        }
    }
}
