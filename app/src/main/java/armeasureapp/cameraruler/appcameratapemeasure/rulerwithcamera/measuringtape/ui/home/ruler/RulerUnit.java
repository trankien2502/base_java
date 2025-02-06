package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ruler;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Anas Altair on 8/29/2018.
 */
public enum RulerUnit {
    CM(2.54f, "CM"),
    IN(1f, "IN");

    private final float converter;
    private final String unit;

    RulerUnit(float converter, String unit) {
        this.converter = converter;
        this.unit = unit;
    }
    public static float mmToPx(float mm, float coefficient, DisplayMetrics displayMetrics) {
        return mm * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, coefficient, displayMetrics);
    }

    public static float pxToIn(float px, float coefficient, DisplayMetrics displayMetrics) {
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, coefficient, displayMetrics);
    }
    public static float inToPx(float in, float coefficient, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, in * coefficient, displayMetrics);
    }


    @SuppressLint("DefaultLocale")
    public String getUnitString(float distance) {
        switch (this) {
            case CM:
                return String.format("%.1f CM",  distance);
            case IN:
                return String.format("%.2f IN", distance).replace(',', '.');
            default:
                return String.valueOf(distance);
        }
    }

    public float convert(float value) {
        return value * converter;
    }
}
