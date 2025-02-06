package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.DialogLoadingBinding;


public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {
    public LoadingDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogLoadingBinding setBinding() {
        return DialogLoadingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void bindView() {

    }
}
