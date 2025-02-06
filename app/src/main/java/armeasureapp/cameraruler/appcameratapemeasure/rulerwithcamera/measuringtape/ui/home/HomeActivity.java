package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityHomeBinding;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.GoToSettingDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.exit.ExitAppDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.exit.IClickDialogExit;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.rate.IClickDialogRate;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.rate.RatingDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ar_ruler.ArRulerActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.protractor.ProtractorActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ruler.RulerActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.tape_measure.TapeMeasureActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.setting.SettingActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.EventTracking;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.PermissionManager;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SPUtils;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SharePrefUtils;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SystemUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {


    private static final int REQUEST_CODE_CAMERA_PERMISSION = 125;
    ArrayList<String> exitRate = new ArrayList<String>(Arrays.asList("2", "4", "6", "8", "10"));
    private int countCamera = 0;

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        countCamera = SPUtils.getInt(this, SPUtils.CAMERA, 0);
        EventTracking.logEvent(this, "home_view");
    }

    @Override
    public void bindView() {
        binding.ivSetting.setOnClickListener(view -> {
            startNextActivity(SettingActivity.class, null);
        });
        binding.clProtractor.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, ProtractorActivity.class));
        });
        binding.cl2dRuler.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, RulerActivity.class));
        });
        binding.cl2dTape.setOnClickListener(view -> {
            if (!PermissionManager.checkCameraPermission(this)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            } else
                resultLauncher.launch(new Intent(this, TapeMeasureActivity.class));
        });
        binding.clArRuler.setOnClickListener(view -> {
            if (!PermissionManager.checkCameraPermission(this)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            } else
                resultLauncher.launch(new Intent(this, ArRulerActivity.class));
        });
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    @Override
    public void onBack() {
        if (!SharePrefUtils.isRated(this)) {
            if (exitRate.contains(String.valueOf(SharePrefUtils.getCountOpenApp(this)))) {
                rateApp();
            } else {
                exitApp();
            }
        } else {
            exitApp();
        }
    }

    private void rateApp() {
        RatingDialog ratingDialog = new RatingDialog(HomeActivity.this, true);
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
                    SharePrefUtils.forceRated(HomeActivity.this);
                    int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                    EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                ReviewManager manager = ReviewManagerFactory.create(HomeActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(HomeActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            //binding.rlRate.setVisibility(View.GONE);
                            int star = SPUtils.getInt(HomeActivity.this, SPUtils.RATE_STAR, 0);
                            EventTracking.logEvent(HomeActivity.this, "rate_submit", "rate_star" + star, String.valueOf(star));
                            SharePrefUtils.forceRated(HomeActivity.this);
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
                EventTracking.logEvent(HomeActivity.this, "rate_not_now");
                ratingDialog.dismiss();
                finishAffinity();
            }

        });
        ratingDialog.show();
        EventTracking.logEvent(this, "rate_show");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        countCamera++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
                        SPUtils.setInt(this, SPUtils.CAMERA, countCamera);
                        if (countCamera > 1) {
                            showDialogGotoSetting(2);
                        }
                    }

                }
            }
        }
    }

    private void showDialogGotoSetting(int type) {
        GoToSettingDialog dialog = new GoToSettingDialog(this, true);
        SystemUtil.setLocale(this);

        if (type == 1) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_noti);
        } else if (type == 2) {
            dialog.binding.tvContent.setText(R.string.content_dialog_per_camera);
        }

        dialog.binding.tvStay.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvContent.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.binding.tvAgree.setOnClickListener(view -> {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            resultLauncher.launch(intent);
        });
        dialog.show();
    }

    private void exitApp() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this, true);
        exitAppDialog.init(new IClickDialogExit() {
            @Override
            public void cancel() {
                exitAppDialog.dismiss();
            }

            @Override
            public void quit() {
                exitAppDialog.dismiss();
                finishAffinity();
            }
        });

        try {
            exitAppDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
