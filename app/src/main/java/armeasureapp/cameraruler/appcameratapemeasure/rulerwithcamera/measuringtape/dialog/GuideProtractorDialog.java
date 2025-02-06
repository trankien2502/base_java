package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.DialogGuideProtractorBinding;


public class GuideProtractorDialog extends BaseDialog<DialogGuideProtractorBinding> {
    public GuideProtractorDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogGuideProtractorBinding setBinding() {
        return DialogGuideProtractorBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
}
