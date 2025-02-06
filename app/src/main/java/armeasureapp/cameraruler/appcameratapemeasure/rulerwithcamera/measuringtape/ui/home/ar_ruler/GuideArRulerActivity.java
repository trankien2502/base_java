package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ar_ruler;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityGuideArRulerBinding;

public class GuideArRulerActivity extends BaseActivity<ActivityGuideArRulerBinding> {

    @Override
    public ActivityGuideArRulerBinding getBinding() {
        return ActivityGuideArRulerBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(view -> onBack());
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}