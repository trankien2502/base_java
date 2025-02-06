package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.DialogGuideRulerBinding;


public class GuideRulerDialog extends BaseDialog<DialogGuideRulerBinding> {
    public GuideRulerDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogGuideRulerBinding setBinding() {
        return DialogGuideRulerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
}
