package com.assistivetouch.easytouch.homebutton.ui.intro;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityIntroBinding;
import com.assistivetouch.easytouch.homebutton.dialog.rate.IClickDialogRate;
import com.assistivetouch.easytouch.homebutton.dialog.rate.RatingDialog;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.permission.PermissionActivity;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SharePrefUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {

    ImageView[] dots = null;
    int positionPage = 0;
    String[] title;
    String[] content;
    private boolean isStartActivity = true;
    List<Integer> listImage;
    SlideAdapter adapter;
    int height;

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        listImage = new ArrayList<>();
//        if (IsNetWork.haveNetworkConnection(this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(getBaseContext())) {
//            binding.circle4.setVisibility(View.VISIBLE);
//            listImage.add(R.drawable.img_intro_1);
//            listImage.add(R.drawable.img_intro_2);
//            listImage.add(R.drawable.img_intro_3);
//            listImage.add(R.drawable.img_intro_3);
//            dots = new ImageView[]{findViewById(R.id.circle1), findViewById(R.id.circle2), findViewById(R.id.circle3), findViewById(R.id.circle4)};
//            title = new String[]{getResources().getString(R.string.intro_1), getResources().getString(R.string.intro_2), getResources().getString(R.string.intro_3), getResources().getString(R.string.intro_3)};
//            content = new String[]{getResources().getString(R.string.content_intro_1), getResources().getString(R.string.content_intro_2), getResources().getString(R.string.content_intro_3), getResources().getString(R.string.content_intro_3)};
//        } else {
//
//        }
        binding.circle4.setVisibility(View.GONE);
        listImage.add(R.drawable.img_intro_1);
        listImage.add(R.drawable.img_intro_2);
        listImage.add(R.drawable.img_intro_3);
        dots = new ImageView[]{findViewById(R.id.circle1), findViewById(R.id.circle2), findViewById(R.id.circle3)};
        title = new String[]{getResources().getString(R.string.intro_1), getResources().getString(R.string.intro_2), getResources().getString(R.string.intro_3)};
        content = new String[]{getResources().getString(R.string.content_intro_1), getResources().getString(R.string.content_intro_2), getResources().getString(R.string.content_intro_3)};
        binding.viewHeight.post(() -> {
            height = binding.viewHeight.getHeight();
            Log.d("intro_check", "Chiều cao của viewHeight: " + height);
            adapter = new SlideAdapter(this, this, listImage, false
                    , height, () -> {
                binding.viewPager.setCurrentItem(3);
            });
            binding.viewPager.setAdapter(adapter);
            binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    positionPage = position;
                    if (!IsNetWork.haveNetworkConnection(IntroActivity.this)) {
                        if (IntroActivity.this.listImage.size() > 3) {
                            listImage.remove(3);
                            adapter.setList(listImage);
                        }
                    }
//                    if (IsNetWork.haveNetworkConnection(IntroActivity.this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(getBaseContext())) {
//                        if (position == 2 || (position == 1 && positionOffset > 0)) {
//                            binding.nativeIntro.setVisibility(View.INVISIBLE);
//                            binding.tvIntro.setVisibility(View.GONE);
//                            binding.tvIntroContent.setVisibility(View.GONE);
//                            binding.rlBottom3.setVisibility(View.GONE);
//                        } else {
//                            if ((position == 0 && positionOffset > 0) || position == 1) {
//                                binding.nativeIntro.setVisibility(View.INVISIBLE);
//                            } else {
//                                if (IsNetWork.haveNetworkConnection(IntroActivity.this) && !ConstantIdAds.listIDAdsNativeIntro.isEmpty() && ConstantRemote.inter_intro && CheckAds.getInstance().isShowAds(getBaseContext())) {
//                                    binding.nativeIntro.setVisibility(View.VISIBLE);
//                                } else {
//                                    binding.nativeIntro.setVisibility(View.INVISIBLE);
//                                }
//                            }
//                            binding.tvIntro.setVisibility(View.VISIBLE);
//                            binding.tvIntroContent.setVisibility(View.VISIBLE);
//                            binding.rlBottom3.setVisibility(View.VISIBLE);
//                        }
//                    }

                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    changeContentInit(position);
//                    if (IsNetWork.haveNetworkConnection(IntroActivity.this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(getBaseContext())) {
//                        if (position == 0) {
//                            EventTracking.logEvent(IntroActivity.this, "Intro1_view");
//                        } else if (position == 1) {
//                            EventTracking.logEvent(IntroActivity.this, "Intro2_view");
//                        } else if (position == 3) {
//                            EventTracking.logEvent(IntroActivity.this, "Intro3_view");
//                        }
//                    } else {
//
//                    }
                    if (position == 0) {
                        EventTracking.logEvent(IntroActivity.this, "Intro1_view");
                    } else if (position == 1) {
                        EventTracking.logEvent(IntroActivity.this, "Intro2_view");
                    } else {
                        EventTracking.logEvent(IntroActivity.this, "Intro3_view");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });
        });


    }

    @Override
    public void bindView() {
        binding.btnNext2.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() == 0) {
                EventTracking.logEvent(IntroActivity.this, "Intro1_next_click");
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else if (binding.viewPager.getCurrentItem() == 1) {
                EventTracking.logEvent(IntroActivity.this, "Intro2_next_click");
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                EventTracking.logEvent(IntroActivity.this, "Intro3_next_click");
                if (isStartActivity) {
                    isStartActivity = false;
                    goToHome();
                }
            }
        });
    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    public void goToHome() {
        startNextActivity(PermissionActivity.class, null);
//        if (SharePrefUtils.getCountOpenApp(IntroActivity.this) == 1) {
//            startNextActivity(PermissionActivity.class, null);
//        } else {
//            if (!PermissionManager.checkNotificationPermission(this) || !PermissionManager.checkCameraPermission(this) || !PermissionManager.checkStoragePermissionSplash(this)) {
//                startNextActivity(PermissionActivity.class, null);
//            } else {
//                if (ConstantRemote.show_permission.contains(String.valueOf(SharePrefUtils.getCountOpenApp(IntroActivity.this))))
//                    startNextActivity(PermissionActivity.class, null);
//                else startNextActivity(HomeActivity.class, null);
//            }
//        }
        finish();
    }

    private void changeContentInit(int position) {
        binding.tvIntro.setText(title[position]);
        binding.tvIntroContent.setText(content[position]);
//        if (IsNetWork.haveNetworkConnection(this) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(getBaseContext())) {
//            for (int i = 0; i < 4; i++) {
//                if (i == position) {
//                    dots[i].setImageResource(R.drawable.ic_intro_s);
//                } else dots[i].setImageResource(R.drawable.ic_intro_sn);
//            }
//        } else {
//
//        }
        if (binding.circle4.getVisibility() == View.VISIBLE) {
            binding.circle4.setVisibility(View.GONE);
        }
        for (int i = 0; i < 3; i++) {
            if (i == position) {
                dots[i].setImageResource(R.drawable.ic_intro_s);
            } else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }
        switch (position) {

        }
    }


    @Override
    protected void onResume() {
//        AppOpenManager.getInstance().enableAppResumeWithActivity(IntroActivity.class);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager.getCurrentItem());
    }
    private void rateApp() {
        RatingDialog ratingDialog = new RatingDialog(IntroActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                //binding.rlRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    finishAffinity();
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(IntroActivity.this);
                    int star = SPUtils.getInt(IntroActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(IntroActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(IntroActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(IntroActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(IntroActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            //binding.rlRate.setVisibility(View.GONE);
                            int star = SPUtils.getInt(IntroActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(IntroActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            SharePrefUtils.forceRated(IntroActivity.this);
                            ratingDialog.dismiss();
                            finishAffinity();
                        });
                    } else {
                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {
                EventTracking.logEvent(IntroActivity.this, "rate_not_now");
                ratingDialog.dismiss();
                finishAffinity();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this, "rate_show");
    }

}
