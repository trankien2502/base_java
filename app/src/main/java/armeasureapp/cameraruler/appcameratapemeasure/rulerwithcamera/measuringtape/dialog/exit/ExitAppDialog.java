package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.exit;

import android.content.Context;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseDialog;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.DialogExitAppBinding;

public class ExitAppDialog extends BaseDialog<DialogExitAppBinding> {
    IClickDialogExit iBaseListener;
    Context context;

    public ExitAppDialog(Context context, Boolean cancelAble) {
        super(context, cancelAble);
        this.context = context;
    }


    @Override
    protected DialogExitAppBinding setBinding() {
        return DialogExitAppBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void bindView() {
        binding.btnDeny.setOnClickListener(view -> iBaseListener.cancel());

        binding.btnAllow.setOnClickListener(view -> iBaseListener.quit());

    }

    public void init(IClickDialogExit iBaseListener) {
        this.iBaseListener = iBaseListener;
    }

}
