package com.livescore.soccerscore.matchlive.ui.livescores.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.databinding.FragmentSettingsBinding;
import com.livescore.soccerscore.matchlive.dialog.rate.IClickDialogRate;
import com.livescore.soccerscore.matchlive.dialog.rate.RatingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.language.LanguageActivity;
import com.livescore.soccerscore.matchlive.ui.setting.AboutActivity;
import com.livescore.soccerscore.matchlive.util.EventTracking;
import com.livescore.soccerscore.matchlive.util.SPUtils;
import com.livescore.soccerscore.matchlive.util.SharePrefUtils;

public class SettingFragment extends BaseFragment<FragmentSettingsBinding> {


    @Override
    public FragmentSettingsBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentSettingsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.tvLangCurrent.setText(SPUtils.getString(requireContext(), SPUtils.LANGUAGE, ""));
        if (SharePrefUtils.isRated(requireContext())) {
            binding.clRate.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        binding.clLanguage.setOnClickListener(view -> {
            EventTracking.logEvent(requireContext(), "setting_language_click");
            startArc(new Intent(requireContext(), LanguageActivity.class));
        });
        binding.clRate.setOnClickListener(view -> onRate());
        binding.clAbout.setOnClickListener(view -> {
            EventTracking.logEvent(requireContext(), "setting_about_click");
            startArc(new Intent(requireContext(), AboutActivity.class));
        });
        binding.clShare.setOnClickListener(view -> onShare());
    }


    private void onRate() {
        EventTracking.logEvent(requireContext(), "setting_rate_us_click");
        RatingDialog ratingDialog = new RatingDialog(requireContext(), true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                binding.clRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    int star = SPUtils.getInt(requireContext(), SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(requireContext(), "rate_submit", "rate_star" + star, String.valueOf(star));
                    //AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
                    SharePrefUtils.forceRated(requireContext());
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                EventTracking.logEvent(requireContext(), "rate_submit");
                ReviewManager manager = ReviewManagerFactory.create(requireContext());
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(requireActivity(), reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            int star = SPUtils.getInt(requireContext(), SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(requireContext(), "rate_submit", "rate_star" + star, String.valueOf(star));
                            binding.clRate.setVisibility(View.GONE);
                            SharePrefUtils.forceRated(requireContext());
                            ratingDialog.dismiss();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(requireContext(), "rate_not_now");
                ratingDialog.dismiss();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(requireContext(), "rate_show");
    }

    private void onShare() {
        EventTracking.logEvent(requireContext(), "setting_share_click");
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :" + "https://play.google.com/store/apps/details?id=" + requireContext().getPackageName());
        startActivity(Intent.createChooser(intentShare, "Share with"));
        //AppOpenManager.getInstance().disableAppResumeWithActivity(SettingActivity.class);
    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }
}