package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.tape_measure;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.base.BaseActivity;
import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.databinding.ActivityTapeMeasureBinding;

import java.util.Arrays;

public class TapeMeasureActivity extends BaseActivity<ActivityTapeMeasureBinding> implements SensorEventListener {

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;
    Preview preview;
    CameraSelector cameraSelector;
    SensorManager sensorManager;
    Sensor accelerateSensor;
    Sensor magenticSensor;
    float[] geomagnetic;
    float[] gravity;
    float[] RR = new float[9];
    float[] orientation = new float[3];
    DisplayMetrics dm;
    double down_angle, up_angle, object_height_from_ground, angle_with_ground, distance_from_object, length_of_object, human_length;

    String rolls;
    int countPlace = 0;

    @Override
    public ActivityTapeMeasureBinding getBinding() {
        return ActivityTapeMeasureBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.clView.setPadding(
                binding.clView.getPaddingLeft(),
                binding.clView.getPaddingTop() + getStatusBarHeight(),
                binding.clView.getPaddingRight(),
                binding.clView.getPaddingBottom()
        );
        startCameraBack();
        down_angle = 0;
        up_angle = 0;
        angle_with_ground = 0;
        countPlace = 0;
        object_height_from_ground = 0;
        length_of_object = 0;
        distance_from_object = 0;
        intialize_variables();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magenticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magenticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(view -> {
            onBack();
        });
        binding.ivMinus.setOnClickListener(view -> {
            binding.sbHumanHeight.setProgress(binding.sbHumanHeight.getProgress() - 1);
        });
        binding.ivPlus.setOnClickListener(view -> {
            binding.sbHumanHeight.setProgress(binding.sbHumanHeight.getProgress() + 1);
        });
        binding.sbHumanHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                human_length = i;
                if (human_length == 0) {
                    human_length = 1;
                }
                if (human_length > 200) {
                    human_length = 200;
                }
                String formattedText = String.format("%.0fcm", human_length);
                String formattedText2 = getString(R.string.height_from_the_ground_to_device) + " " + formattedText;
                SpannableStringBuilder spannable = new SpannableStringBuilder(formattedText2);
                spannable.setSpan(new StyleSpan(Typeface.BOLD), formattedText2.length() - formattedText.length(), formattedText2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.resultView.tvHumanHeight.setText(formattedText);
                binding.tvHeight.setText(spannable);
                if (i > 0) {
                    if (distance_from_object > 0 && length_of_object > 0) {
                        object_calculations_doesnt_touch_ground();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.ivReset.setOnClickListener(view -> {
            binding.resultView.tvHumanHeight.setText(String.format("%.0f", human_length) + "cm");
            binding.resultView.tvDistanceToObject.setVisibility(View.INVISIBLE);
            binding.resultView.tvObjectHeight.setVisibility(View.INVISIBLE);
            binding.resultView.tvDistanceObjectHeight.setVisibility(View.INVISIBLE);
            binding.resultView.ivResultHeight.setImageResource(R.drawable.img_tape_measure_step1);
            binding.clSbHeight.setVisibility(View.VISIBLE);
            down_angle = 0;
            up_angle = 0;
            angle_with_ground = 0;
            countPlace = 0;
            object_height_from_ground = 0;
            length_of_object = 0;
            distance_from_object = 0;
            binding.ivRuler.setImageResource(R.drawable.img_ruler_1);
        });
        binding.ivGuide.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(this, GuideTapeMeasureActivity.class));
        });
        binding.ivRuler.setOnClickListener(view -> {
            binding.clSbHeight.setVisibility(View.INVISIBLE);
            take_angles();
            if (countPlace < 3) {
                countPlace++;
                if (countPlace == 1) {
                    binding.resultView.ivResultHeight.setImageResource(R.drawable.img_tape_measure_step2);
                    binding.ivRuler.setImageResource(R.drawable.img_ruler_2);
                }
                if (countPlace == 2) {
                    binding.resultView.ivResultHeight.setImageResource(R.drawable.img_tape_measure_step3);
                    binding.ivRuler.setImageResource(R.drawable.img_ruler_2);
                }
                if (countPlace == 3) {
                    binding.resultView.ivResultHeight.setImageResource(R.drawable.img_tape_measure_step4);
                    binding.ivRuler.setImageResource(R.drawable.img_ruler_reset);
                    binding.resultView.tvDistanceToObject.setVisibility(View.VISIBLE);
                    binding.resultView.tvObjectHeight.setVisibility(View.VISIBLE);
                    binding.resultView.tvDistanceObjectHeight.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void bindPreViewToCaptureImage() {
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) TapeMeasureActivity.this, cameraSelector, preview);
    }

    private void startCameraBack() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    preview = new Preview.Builder().build();
                    cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();
                    preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
                    bindPreViewToCaptureImage();
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {

            }
        }
    });

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, magenticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerateSensor);
        sensorManager.unregisterListener(this, magenticSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent Event) {
        if (Event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = Event.values.clone();
        else if (Event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = Event.values.clone();
//        if (gravity==null){
//            gravity = new float[]{0,0,0};
//        }
//        if (geomagnetic==null){
//            geomagnetic = new float[]{0,0,0};
//        }
        if (geomagnetic != null && gravity != null) {
            SensorManager.getRotationMatrix(RR, null, gravity, geomagnetic);
            SensorManager.getOrientation(RR, orientation);
        }
        Log.e("check_sensor", "geomagnetic: " + Arrays.toString(geomagnetic));
        Log.e("check_sensor", "gravity: " + Arrays.toString(gravity));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void base_case() {
        down_angle = Math.abs(down_angle);
        up_angle = Math.abs(up_angle);
        distance_from_object = human_length / Math.tan(Math.toRadians(down_angle));
        length_of_object = human_length + Math.tan(Math.toRadians(up_angle)) * distance_from_object;


        if (length_of_object / 100 > 0) {
            binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
            binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
//            binding.resultView.tvHumanHeight.setText(object_height_from_ground+"cm");

            binding.resultView.tvObjectHeight.setVisibility(View.VISIBLE);
            binding.resultView.tvObjectHeight.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getString(R.string.Move_Forward), Toast.LENGTH_LONG).show();
            down_angle = 0;
            up_angle = 0;
        }
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void measure_small_object() {
        down_angle = Math.abs(down_angle);
        up_angle = Math.abs(up_angle);
        double distance_angle = 90 - down_angle;
        distance_from_object = human_length * Math.tan(Math.toRadians(distance_angle));
        Log.e("angle:", String.valueOf(down_angle) + "-" + String.valueOf(up_angle));
        double part_of_my_tall = distance_from_object * Math.tan(Math.toRadians(up_angle));
        length_of_object = human_length - part_of_my_tall;
        if (length_of_object / 100 > 0) {
            binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
            binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
//            binding.resultView.tvHumanHeight.setText(object_height_from_ground+"cm");

            binding.resultView.tvObjectHeight.setVisibility(View.VISIBLE);
            binding.resultView.tvDistanceToObject.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Move Forward", Toast.LENGTH_LONG).show();
            down_angle = 0;
            up_angle = 0;
        }

    }


    void take_angles() {
        rolls += String.valueOf((Math.toDegrees(orientation[2])) % 360 + 90) + "\n";
        if (angle_with_ground == 0)
            angle_with_ground = adjust_angle_rotation(Math.toDegrees(orientation[2]) % 360 + 90);
        else if (down_angle == 0)
            down_angle = adjust_angle_rotation(Math.toDegrees(orientation[2]) % 360 + 90);
        else if (up_angle == 0) {
            up_angle = adjust_angle_rotation(Math.toDegrees(orientation[2]) % 360 + 90);
            object_calculations_doesnt_touch_ground();
        }
    }


    double adjust_angle_rotation(double angle) {
        double temp;
        temp = angle;
        if (temp > 90) {
            temp = 180 - temp;
        }
        return temp;
    }


    void object_calculations_touch_ground() {

        if (down_angle < 0 && up_angle > 0)//base case
        {
            double temp = up_angle;
            up_angle = down_angle;
            down_angle = temp;
            base_case();
        } else if ((down_angle > 0 && up_angle > 0) && (down_angle < up_angle))//smaller object
        {
            double temp = up_angle;
            up_angle = down_angle;
            down_angle = temp;
            measure_small_object();
        } else if (up_angle < 0 && down_angle > 0)//base case
            base_case();
        else //smaller object
            measure_small_object();
    }


    void object_calculations_doesnt_touch_ground() {
        if (angle_with_ground > 0 && down_angle > 0 && up_angle < 0) object_on_eyes_level_calc();
        else if (angle_with_ground > 0 && down_angle < 0 && up_angle < 0)
            object_upper_eyes_level_calc();
        else if (angle_with_ground > 0 && down_angle > 0 && up_angle > 0)
            object_below_eyes_level_calc();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void object_on_eyes_level_calc() {
        down_angle = Math.abs(down_angle);
        up_angle = Math.abs(up_angle);
        angle_with_ground = 90 - angle_with_ground;
        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
        double part_down = distance_from_object * Math.tan(Math.toRadians(down_angle));
        double part_up = distance_from_object * Math.tan(Math.toRadians(up_angle));
        length_of_object = part_down + part_up;
        object_height_from_ground = human_length - part_down;

        if (length_of_object < 0) {
            length_of_object = 0;
        }
        if (distance_from_object < 0) {
            distance_from_object = 0;
        }
        if (object_height_from_ground < 0) {
            object_height_from_ground = 0;
        }

        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");

//        tvLength.setVisibility(View.VISIBLE);
//        tvWidth.setVisibility(View.VISIBLE);
//        tvDistance.setVisibility(View.VISIBLE);

    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void object_upper_eyes_level_calc() {
        down_angle = Math.abs(down_angle);
        up_angle = Math.abs(up_angle);

        angle_with_ground = 90 - angle_with_ground;
        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
        double part = distance_from_object * Math.tan(Math.toRadians(down_angle));
        double all = distance_from_object * Math.tan(Math.toRadians(up_angle));
        length_of_object = all - part;
        object_height_from_ground = human_length + part;

        if (length_of_object < 0) {
            length_of_object = 0;
        }
        if (distance_from_object < 0) {
            distance_from_object = 0;
        }
        if (object_height_from_ground < 0) {
            object_height_from_ground = 0;
        }
        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");

//        tvLength.setVisibility(View.VISIBLE);
//        tvWidth.setVisibility(View.VISIBLE);
//        tvDistance.setVisibility(View.VISIBLE);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void object_below_eyes_level_calc() {
        down_angle = Math.abs(down_angle);
        up_angle = Math.abs(up_angle);
        angle_with_ground = 90 - angle_with_ground;
        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
        double all = distance_from_object * Math.tan(Math.toRadians(down_angle));
        double part = distance_from_object * Math.tan(Math.toRadians(up_angle));
        length_of_object = all - part;
        object_height_from_ground = human_length - all;

        if (length_of_object < 0) {
            length_of_object = 0.1;
        }
        if (distance_from_object < 0) {
            distance_from_object = 0.1;
        }
        if (object_height_from_ground < 0) {
            object_height_from_ground = 0.1;
        }
        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");

//        tvLength.setVisibility(View.VISIBLE);
//        tvWidth.setVisibility(View.VISIBLE);
//        tvDistance.setVisibility(View.VISIBLE);
    }
//    @SuppressLint({"SetTextI18n", "DefaultLocale"})
//    void object_on_eyes_level_calc() {
//        down_angle = Math.abs(down_angle);
//        up_angle = Math.abs(up_angle);
//        angle_with_ground = 90 - angle_with_ground;
//        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
//
//        double part_down = distance_from_object * Math.tan(Math.toRadians(down_angle));
//        double part_up = distance_from_object * Math.tan(Math.toRadians(up_angle));
//        length_of_object = part_down + part_up;
//        object_height_from_ground = human_length - part_down;
//        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
//        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
//        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");
//    }
//
//
//    @SuppressLint({"SetTextI18n", "DefaultLocale"})
//    void object_upper_eyes_level_calc() {
//        down_angle = Math.abs(down_angle);
//        up_angle = Math.abs(up_angle);
//
//        angle_with_ground = 90 - angle_with_ground;
//        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
//        double part = distance_from_object * Math.tan(Math.toRadians(down_angle));
//        double all = distance_from_object * Math.tan(Math.toRadians(up_angle));
//        length_of_object = all - part;
//        object_height_from_ground = human_length + part;
//        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
//        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
//        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");
//    }
//
//
//    @SuppressLint({"DefaultLocale", "SetTextI18n"})
//    void object_below_eyes_level_calc() {
//        down_angle = Math.abs(down_angle);
//        up_angle = Math.abs(up_angle);
//        angle_with_ground = 90 - angle_with_ground;
//        distance_from_object = human_length * Math.tan(Math.toRadians(angle_with_ground));
//        double all = distance_from_object * Math.tan(Math.toRadians(down_angle));
//        double part = distance_from_object * Math.tan(Math.toRadians(up_angle));
//        length_of_object = all - part;
//        object_height_from_ground = human_length - all;
//        binding.resultView.tvObjectHeight.setText(String.format("%.1f", length_of_object) + "cm");
//        binding.resultView.tvDistanceToObject.setText(String.format("%.1f", distance_from_object) + "cm");
//        binding.resultView.tvDistanceObjectHeight.setText(String.format("%.1f", object_height_from_ground) + "cm");
//    }

    void intialize_variables() {
        rolls = "";
        down_angle = 0;
        up_angle = 0;
        angle_with_ground = 0;
        human_length = 80;
        binding.sbHumanHeight.setProgress(80);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        @SuppressLint("DefaultLocale") String formattedText = String.format("%.0fcm", human_length);
        String formattedText2 = getString(R.string.height_from_the_ground_to_device) + " " + formattedText;
        SpannableStringBuilder spannable = new SpannableStringBuilder(formattedText2);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), formattedText2.length() - formattedText.length(), formattedText2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.resultView.tvHumanHeight.setText(formattedText);
        binding.tvHeight.setText(spannable);
    }
}