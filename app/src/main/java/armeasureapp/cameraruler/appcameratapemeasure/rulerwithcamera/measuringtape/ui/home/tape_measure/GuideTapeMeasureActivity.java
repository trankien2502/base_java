package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.tape_measure;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityGuideTapeMeasureBinding;

public class GuideTapeMeasureActivity extends BaseActivity<ActivityGuideTapeMeasureBinding> {

    @Override
    public ActivityGuideTapeMeasureBinding getBinding() {
        return ActivityGuideTapeMeasureBinding.inflate(getLayoutInflater());
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