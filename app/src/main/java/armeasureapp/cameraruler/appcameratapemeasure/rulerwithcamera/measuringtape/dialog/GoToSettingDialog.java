package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.DialogPermissionBinding;


public class GoToSettingDialog extends BaseDialog<DialogPermissionBinding> {
    public GoToSettingDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
    }

    @Override
    protected DialogPermissionBinding setBinding() {
        return DialogPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {

    }
}
