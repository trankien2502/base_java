package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ruler;

import android.annotation.SuppressLint;
import android.view.View;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityRulerBinding;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.dialog.GuideRulerDialog;

public class RulerActivity extends BaseActivity<ActivityRulerBinding> {

    private static final int STATE_POINT = 1;
    private static final int STATE_SQUARE = 2;
    private static final int STATE_NORMAL = 3;
    private static final int STATE_2D = 4;
    private static final int STATE_CM = 3;
    private static final int STATE_INCH = 4;
    int stateDesign = STATE_SQUARE;
    int stateRuler = STATE_NORMAL;
    int stateCmInch = STATE_CM;

    @Override
    public ActivityRulerBinding getBinding() {
        return ActivityRulerBinding.inflate(getLayoutInflater());
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void initView() {
        showDialogMeasure();
        changeState();
        binding.rulerView.setOnRulerChangeListener((cm, inches) -> {
            String inchesString = inches == 0 ? "0" : String.format("%.3f", inches);
            String cmString = cm == 0 ? "0" : String.format("%.1f", cm);
            binding.txtValueCm.setText(cmString + " cm");
            binding.txtValueIn.setText(inchesString + " in");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showDialogMeasure() {
        GuideRulerDialog dialog = new GuideRulerDialog(this, false);
        dialog.binding.btnAllow.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(view -> onBack());
        binding.btnNormal.setOnClickListener(view -> {
            stateRuler = STATE_NORMAL;
            changeState();
        });
        binding.btn2D.setOnClickListener(view -> {
            stateRuler = STATE_2D;
            changeState();
        });
        binding.ivStyle.setOnClickListener(view -> {
            if (stateDesign == STATE_SQUARE) {
                stateDesign = STATE_POINT;
            } else {
                stateDesign = STATE_SQUARE;
            }
            changeState();
        });
        binding.ivInchCm.setOnClickListener(view -> {
            if (stateCmInch == STATE_CM) {
                stateCmInch = STATE_INCH;
            } else {
                stateCmInch = STATE_CM;
            }
            changeState();
        });
    }

    private void changeState() {
        if (stateRuler == STATE_2D) {
            if (stateDesign == STATE_SQUARE) {
                binding.ivStyle.setImageResource(R.drawable.ruler_2d);
                binding.ruler2dView.setDragging(true);
            } else {
                binding.ivStyle.setImageResource(R.drawable.ruler_normal);
                binding.ruler2dView.setDragging(false);
            }
            if (stateCmInch == STATE_INCH) {
                binding.ivInchCm.setImageResource(R.drawable.img_inch);
                binding.ruler2dView.setCmState(false);
            } else {
                binding.ivInchCm.setImageResource(R.drawable.img_cm);
                binding.ruler2dView.setCmState(true);
            }
            binding.rulerView.setVisibility(View.GONE);
            binding.ruler2dView.setVisibility(View.VISIBLE);
            binding.llViewResult.setVisibility(View.GONE);
            binding.ivInchCm.setVisibility(View.VISIBLE);
            binding.btn2D.setBackgroundResource(R.drawable.bg_camera_access_allow);
            binding.btnNormal.setBackgroundResource(0);
        } else {
            if (stateDesign == STATE_SQUARE) {
                binding.ivStyle.setImageResource(R.drawable.ruler_2d);
                binding.rulerView.setDragging(true);
            } else {
                binding.ivStyle.setImageResource(R.drawable.ruler_normal);
                binding.rulerView.setDragging(false);
            }
            binding.rulerView.setVisibility(View.VISIBLE);
            binding.ruler2dView.setVisibility(View.GONE);
            binding.llViewResult.setVisibility(View.VISIBLE);
            binding.ivInchCm.setVisibility(View.GONE);
            binding.btnNormal.setBackgroundResource(R.drawable.bg_camera_access_allow);
            binding.btn2D.setBackgroundResource(0);
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }
}