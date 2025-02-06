package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ruler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;

import java.util.Objects;

public class RulerView extends View {

    private static final int UpperSection = 1;
    private static final int LowerSection = 2;
    private RulerUnit unit = RulerUnit.CM;
    private Paint colorPaintMask;
    private Paint grayPaint;
    private Paint grayPaintReplace;
    private Paint middleSectionPaint;
    private float upperY = 0f;
    private float lowerY = 1f;
    private final float minDistance = dpTOpx(0f);
    private int currentSection = 0;
    private float pointerY = 5f;
    private final float coefficient = 1f;
    private final float markCmWidth = dpTOpx(40f);
    private final float markHalfCmWidth = dpTOpx(21f);
    private final float markMmWidth = dpTOpx(10f);
    private Paint textPaint;

    private Paint topValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bottomValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint lineOrientationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean isDraging = true;

    public void setDragging(boolean dragging) {
        this.isDraging = dragging;
        if (!isDraging) upperY = offsetTop;
        invalidate();
        notifyDistanceChangeListener();
    }

    private float offsetTop = 0;
    private float offsetBottom = 0;
    float oneMmInPx = 0;

    @SuppressLint("ResourceAsColor")
    private void init() {
        oneMmInPx = RulerUnit.mmToPx(1f, coefficient, getResources().getDisplayMetrics());
        offsetTop = oneMmInPx * 5;
        colorPaintMask = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaintReplace = new Paint(grayPaint);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorPaintMask = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleSectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_700);
        textPaint.setTypeface(typeface);

        grayPaintReplace.setColor(Color.BLACK);
        grayPaint.setColor(Color.BLACK);
        middleSectionPaint.setColor(Color.parseColor("#FFDC31"));
        colorPaintMask.setColor(Color.parseColor("#31990D"));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(dpTOpx(14));
        topValuePaint.setColor(Color.parseColor("#00C1AE00"));

        bottomValuePaint.setColor(Color.parseColor("#00C1AE00"));
        lineOrientationPaint.setColor(Color.parseColor("#866F00"));
        lineOrientationPaint.setStrokeWidth(dpTOpx(1));
    }

    private boolean firstRun = false;
    private float centerWidth = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!firstRun) {
            if (isDraging)
                upperY = offsetTop + 10 * oneMmInPx;
            else upperY = offsetTop;
            lowerY = canvas.getHeight() - 10 * oneMmInPx;
            firstRun = true;
            centerWidth = canvas.getWidth() / 2;
            width = canvas.getWidth();
            height = canvas.getHeight();
            notifyDistanceChangeListener();
        }
        if (isDraging) {
//            canvas.drawRect(0f, offsetTop, getWidth(), upperY, topValuePaint);
//            canvas.drawRect(0f, canvas.getHeight(), getWidth(), lowerY, bottomValuePaint);
            canvas.drawRect(0f, upperY, getWidth(), lowerY, middleSectionPaint);

            canvas.drawLine(0, upperY, getWidth(), upperY, lineOrientationPaint);
            drawRoundRectPath(canvas, new RectF(centerWidth - width * 0.1f, upperY - 5, centerWidth + width * 0.1f, upperY + 5),
                    5f, true, true, true, true, lineOrientationPaint);

            canvas.drawLine(0, lowerY, getWidth(), lowerY, lineOrientationPaint);
            drawRoundRectPath(canvas, new RectF(centerWidth - width * 0.1f, lowerY - 5, centerWidth + width * 0.1f, lowerY + 5),
                    5f, true, true, true, true, lineOrientationPaint);
        } else {
            canvas.drawLine(0, upperY, getWidth(), upperY, lineOrientationPaint);

            canvas.drawLine(0, lowerY, getWidth(), lowerY, lineOrientationPaint);
            drawRoundRectPath(canvas, new RectF(centerWidth - width * 0.1f, lowerY - 5, centerWidth + width * 0.1f, lowerY + 5),
                    5f, true, true, true, true, lineOrientationPaint);
        }
        drawMarksInCm(canvas, grayPaintReplace);
        drawMarksInIn(canvas, grayPaintReplace);
        String cm = "cm";
        String inch = "inch";
        canvas.drawText("cm", 5f, textPaint.measureText(cm) * 0.75f, textPaint);
        canvas.drawText("inch", canvas.getWidth() - textPaint.measureText(inch) - 5, textPaint.measureText(cm) * 0.75f, textPaint);

    }


    public void drawRoundRectPath(Canvas canvas, RectF rectF, float radius,
                                  boolean topLeft, boolean topRight,
                                  boolean bottomLeft, boolean bottomRight,
                                  Paint paint) {
        Path path = new Path();

        if (bottomRight) {
            path.moveTo(rectF.left, rectF.bottom - radius);
        } else {
            path.moveTo(rectF.left, rectF.bottom);
        }

        if (topLeft) {
            path.lineTo(rectF.left, rectF.top + radius);
            path.quadTo(rectF.left, rectF.top, rectF.left + radius, rectF.top);
        } else {
            path.lineTo(rectF.left, rectF.top);
        }

        if (topRight) {
            path.lineTo(rectF.right - radius, rectF.top);
            path.quadTo(rectF.right, rectF.top, rectF.right, rectF.top + radius);
        } else {
            path.lineTo(rectF.right, rectF.top);
        }

        if (bottomRight) {
            path.lineTo(rectF.right, rectF.bottom - radius);
            path.quadTo(rectF.right, rectF.bottom, rectF.right - radius, rectF.bottom);
        } else {
            path.lineTo(rectF.right, rectF.bottom);
        }

        if (bottomLeft) {
            path.lineTo(rectF.left + radius, rectF.bottom);
            path.quadTo(rectF.left, rectF.bottom, rectF.left, rectF.bottom - radius);
        } else {
            path.lineTo(rectF.left, rectF.bottom);
        }

        path.close();

        canvas.drawPath(path, paint);
    }


    private void drawMarksInCm(Canvas canvas, Paint paint) {
        float oneMmInPx = RulerUnit.mmToPx(1f, coefficient, getResources().getDisplayMetrics());

        for (int i = 0; i <= 1000; i++) {
            float y = offsetTop + oneMmInPx * i;
            float markWidth;

            if (i % 10 == 0) {
                markWidth = markCmWidth;
                String text = String.valueOf(i / 10);
                canvas.drawText(text, markWidth + dpTOpx(8), y + dpTOpx(6), textPaint);

            } else if (i % 5 == 0) {
                markWidth = markHalfCmWidth;
            } else {
                markWidth = markMmWidth;
            }

            canvas.drawLine(0f, y, markWidth, y, paint);

            if (y >= getHeight()) {
                break;
            }
        }
    }

    private void drawMarksInIn(Canvas canvas, Paint paint) {
        float oneInchInPx = RulerUnit.inToPx(1f, coefficient, getResources().getDisplayMetrics());
        float offset = RulerUnit.mmToPx(5f, coefficient, getResources().getDisplayMetrics()); // 0.5 cm in px

        int totalInches = 100;
        int subdivisions = 10;

        for (int i = 0; i <= totalInches; i++) {
            for (int j = 0; j < subdivisions; j++) {
                float y = offset + oneInchInPx * i + (oneInchInPx / subdivisions) * j;
                float markWidth;
                if (j == 0) {
                    markWidth = markCmWidth;

                    canvas.drawText(String.valueOf(i), getWidth() - markWidth - dpTOpx(16), y + dpTOpx(6), textPaint);

                } else if (j == subdivisions / 2) {
                    markWidth = markHalfCmWidth;
                } else {
                    markWidth = markMmWidth;
                }

                canvas.drawLine(getWidth(), y, getWidth() - markWidth, y, paint);

                if (y >= getHeight()) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float centerPoint = (lowerY + upperY) / 2;
                if (isDraging)
                    currentSection = (event.getY() < centerPoint) ? UpperSection : LowerSection;
                else currentSection = LowerSection;
                pointerY = event.getY();
                return currentSection != 0;
            case MotionEvent.ACTION_MOVE:
                if (isDraging) {
                    float dy = event.getY() - pointerY;
                    switch (currentSection) {
                        case UpperSection:
                            upperY += dy;
                            upperY = Math.max(0f, Math.min(lowerY - minDistance, upperY));
                            break;
                        case LowerSection:
                            lowerY += dy;
                            lowerY = Math.max(upperY + minDistance, Math.min(getHeight(), lowerY));
                            break;
                    }
                    pointerY = event.getY();
                    if (upperY < offsetTop) {
                        upperY = offsetTop;
                    }
                    if (lowerY > height) {
                        lowerY = height;
                    }
                    if (lowerY < offsetTop) {
                        lowerY = offsetTop;
                    }
                    notifyDistanceChangeListener();
                    invalidate();
                } else {
                    float dy = event.getY() - pointerY;
                    lowerY += dy;
                    lowerY = Math.max(upperY + minDistance, Math.min(getHeight(), lowerY));
                    pointerY = event.getY();
                    if (lowerY > height) {
                        lowerY = height;
                    }
                    if (lowerY < offsetTop) {
                        lowerY = offsetTop;
                    }
                    notifyDistanceChangeListener();
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return false;
    }

    private int width = 0;
    private int height = 0;

    private void notifyDistanceChangeListener() {
        if (onRulerChangeListener != null) {
            onRulerChangeListener.onRulerChange(getDistanceCm(), getDistanceIn());
        }
    }

    public float getDistanceCm() {
        float distanceInInches = RulerUnit.pxToIn(Math.abs(upperY - lowerY), coefficient, getResources().getDisplayMetrics());
        if (Objects.requireNonNull(unit) == RulerUnit.CM) {
            return distanceInInches * 2.54f;
        }
        return distanceInInches;
    }

    public float getDistanceIn() {
        return RulerUnit.pxToIn(Math.abs(upperY - lowerY), coefficient, getResources().getDisplayMetrics());
    }

    public interface OnRulerChangeListener {
        void onRulerChange(float cm, float inches);
    }

    public void setUnitAndUpdate(RulerUnit unit) {
        this.unit = unit;
        notifyDistanceChangeListener();
        invalidate();
    }

    private OnRulerChangeListener onRulerChangeListener;

    public void setOnRulerChangeListener(OnRulerChangeListener listener) {
        this.onRulerChangeListener = listener;
        notifyDistanceChangeListener();
    }

    private float dpTOpx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

}
