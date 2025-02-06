package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.protractor;

import android.annotation.SuppressLint;
import android.view.View;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityProtractorBinding;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.GuideProtractorDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SPUtils;

public class ProtractorActivity extends BaseActivity<ActivityProtractorBinding> {

    @Override
    public ActivityProtractorBinding getBinding() {
        return ActivityProtractorBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void initView() {
        if (!SPUtils.getBoolean(this, SPUtils.GUIDE_PROTRACTOR, false)) {
            binding.clViewFirstGuide.setVisibility(View.VISIBLE);
        } else binding.clViewFirstGuide.setVisibility(View.GONE);
        binding.protractor.setProtractorChanged(angle -> {
            binding.txtValueProtractor.setText(String.format("%.1f °", angle));
        });
    }

    @Override
    public void bindView() {
        binding.clViewFirstGuide.setOnClickListener(view -> {
            binding.clViewFirstGuide.setVisibility(View.GONE);
            SPUtils.setBoolean(this, SPUtils.GUIDE_PROTRACTOR, true);
        });
        binding.ivBack.setOnClickListener(view -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(view -> {
            showDialogGuide();
        });
    }

    private void showDialogGuide() {
        GuideProtractorDialog dialog = new GuideProtractorDialog(this, false);
        dialog.binding.btnAllow.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onBack() {
        if (binding.clViewFirstGuide.getVisibility() == View.VISIBLE) {
            binding.clViewFirstGuide.setVisibility(View.GONE);
            SPUtils.setBoolean(this, SPUtils.GUIDE_PROTRACTOR, true);
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }
}