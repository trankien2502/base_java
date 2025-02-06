package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.ruler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;

import java.util.Objects;

public class Ruler2DView extends View {

    private static final int UPPER_SECTION_VERTICAL = 1;
    private static final int LOWER_SECTION_VERTICAL = 2;
    private static final int LEFT_SECTION_HORIZONTAL = 3;
    private static final int RIGHT_SECTION_HORIZONTAL = 4;
    private RulerUnit unit = RulerUnit.CM;
    private Paint grayPaintReplace;
    private Paint middleSectionPaint;
    private float upperY = 0f;
    private float lowerY = 1f;
    private float leftX = 0f;
    private float rightX = 1f;
    PointF x1, x2, x3, x4;
    private final float minDistance = dpTOpx(0f);
    private int currentSection = 0;
    private float pointerY = 5f;
    private float pointerX = 5f;
    PointF pointer = new PointF();
    private final float coefficient = 1f;
    private final float markCmWidth = dpTOpx(40f);
    private final float markHalfCmWidth = dpTOpx(27f);
    private final float markMmWidth = dpTOpx(10f);
    private Paint textPaint;
    private Paint textPaintResult;
    private Paint textPaintCm;
    private boolean isDraging = true;
    private boolean isCmState = true;

    private Paint topValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bottomValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint lineOrientationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Ruler2DView(Context context) {
        this(context, null);
    }

    public Ruler2DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Ruler2DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setCmState(boolean cmState) {
        this.isCmState = cmState;
        invalidate();
        notifyDistanceChangeListener();
    }

    public boolean isCmState() {
        return isCmState;
    }

    public void setDragging(boolean dragging) {
        this.isDraging = dragging;
        if (!isDraging) {
            upperY = offsetTop;
            leftX = 0;
        }
        invalidate();
        notifyDistanceChangeListener();
    }

    private float offsetTop = 0;
    float oneMmInPx = 0;

    @SuppressLint("ResourceAsColor")
    private void init() {
        x1 = new PointF(0, 0);
        x2 = new PointF(getWidth(), 0);
        x3 = new PointF(0, getHeight());
        x4 = new PointF(getWidth(), getHeight());
        oneMmInPx = RulerUnit.mmToPx(1f, coefficient, getResources().getDisplayMetrics());
        offsetTop = oneMmInPx * 5;
        grayPaintReplace = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintResult = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintCm = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleSectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleSectionPaint.setStyle(Paint.Style.FILL);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_700);
        Typeface typeface1 = ResourcesCompat.getFont(getContext(), R.font.inter_400);
        textPaint.setTypeface(typeface);
        textPaintResult.setTypeface(typeface1);
        textPaintCm.setTypeface(typeface);

        grayPaintReplace.setColor(Color.BLACK);
        middleSectionPaint.setColor(Color.parseColor("#FFDC31"));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(dpTOpx(14));
        textPaintCm.setColor(Color.BLACK);
        textPaintCm.setTextSize(dpTOpx(14));
        textPaint.setTextAlign(Paint.Align.CENTER);

        textPaintResult.setColor(Color.WHITE);
        textPaintResult.setTextSize(dpTOpx(20));
        textPaintResult.setTextAlign(Paint.Align.CENTER);
        topValuePaint.setColor(Color.parseColor("#00C1AE00"));

        bottomValuePaint.setColor(Color.parseColor("#00C1AE00"));
        lineOrientationPaint.setColor(Color.parseColor("#866F00"));
        lineOrientationPaint.setStrokeWidth(dpTOpx(1));
    }

    private boolean firstRun = false;
    private float centerWidth = 0;
    private float centerHeight = 0;

    @SuppressLint({"DrawAllocation", "DefaultLocale"})
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!firstRun) {
            if (isDraging) {
                upperY = offsetTop + 10 * oneMmInPx;
                leftX = 10 * oneMmInPx;
            } else {
                upperY = offsetTop;
                leftX = 0;
            }
            rightX = getWidth() - 10 * oneMmInPx;
            lowerY = getHeight() - 10 * oneMmInPx;
            firstRun = true;
            width = getWidth();
            height = getHeight();
            notifyDistanceChangeListener();
        }
        centerWidth = (float) (rightX + leftX) / 2;
        centerHeight = (float) (lowerY + upperY) / 2;

        x1.set(leftX, upperY);
        x2.set(rightX, upperY);
        x3.set(leftX, lowerY);
        x4.set(rightX, lowerY);

        Log.e("pointcheck", "x1: " + x1.x + " " + x1.y);
        Log.e("pointcheck", "x2: " + x2.x + " " + x2.y);
        Log.e("pointcheck", "x3: " + x3.x + " " + x3.y);
        Log.e("pointcheck", "x4: " + x4.x + " " + x4.y);
        if (isDraging) {
//            canvas.drawRect(0f, offsetTop, getWidth(), upperY, topValuePaint);
//            canvas.drawRect(0f, canvas.getHeight(), getWidth(), lowerY, bottomValuePaint);
//            canvas.drawRect(0f, upperY, getWidth(), lowerY, middleSectionPaint);
            Path path = new Path();
            path.moveTo(x1.x, x1.y);
            path.lineTo(x2.x, x2.y);
            path.lineTo(x4.x, x4.y);
            path.lineTo(x3.x, x3.y);
            canvas.drawPath(path, middleSectionPaint);
            path.close();

            // Vẽ hình chữ nhật với màu vàng
            canvas.drawPath(path, middleSectionPaint);
            canvas.drawLine(leftX, upperY, rightX, upperY, lineOrientationPaint);
            canvas.drawLine(leftX, lowerY, rightX, lowerY, lineOrientationPaint);
            canvas.drawLine(leftX, upperY, leftX, lowerY, lineOrientationPaint);
            canvas.drawLine(rightX, upperY, rightX, lowerY, lineOrientationPaint);

            drawRoundRectPath(canvas, new RectF(((rightX - leftX) - dpTOpx(80f)) >= 0 ? centerWidth - dpTOpx(40f) : centerWidth - (rightX - leftX) / 2, upperY - 5, (rightX - leftX - dpTOpx(80f)) > 0 ? centerWidth + dpTOpx(40f) : centerWidth + (rightX - leftX) / 2, upperY + 5),
                    5f, true, true, true, true, lineOrientationPaint);
            drawRoundRectPath(canvas, new RectF(centerWidth - dpTOpx(50f), lowerY - dpTOpx(16f), centerWidth + dpTOpx(50f), lowerY + dpTOpx(16f)),
                    dpTOpx(18f), true, true, true, true, lineOrientationPaint);


            drawRoundRectPath2(canvas, new RectF(rightX - dpTOpx(16f), centerHeight + dpTOpx(50f), rightX + dpTOpx(16f), centerHeight - dpTOpx(50f)),
                    dpTOpx(18f), true, true, true, true, lineOrientationPaint);
            drawRoundRectPath2(canvas, new RectF(leftX - 5,
                            ((lowerY - upperY) - dpTOpx(80f)) >= 0 ? centerHeight + dpTOpx(40f) : centerHeight + (lowerY - upperY) / 2,
                            leftX + 5,
                            (lowerY - upperY - dpTOpx(80f)) > 0 ? centerHeight - dpTOpx(40f) : centerHeight - (lowerY - upperY) / 2),
                    5f, true, true, true, true, lineOrientationPaint);

            String resultWidth = String.format("%.1f", getDistanceCmWidth()) + " cm";
            if (!isCmState) resultWidth = String.format("%.3f", getDistanceInWidth()) + " in";
            Paint.FontMetrics fontMetrics = textPaintResult.getFontMetrics();
            float textHeight = Math.abs(fontMetrics.top - fontMetrics.bottom);
            canvas.drawText(resultWidth, centerWidth, lowerY + textHeight / 4, textPaintResult);

            canvas.save();
            canvas.rotate(-90, rightX, centerHeight);
            String resultHeight = String.format("%.1f", getDistanceCm()) + " cm";
            if (!isCmState) resultHeight = String.format("%.3f", getDistanceIn()) + " in";
            canvas.drawText(resultHeight, rightX, centerHeight + textHeight / 4, textPaintResult);
            canvas.restore();
        } else {
            canvas.drawLine(leftX, lowerY, getWidth(), lowerY, lineOrientationPaint);
            canvas.drawLine(rightX, upperY, rightX, getHeight(), lineOrientationPaint);
            canvas.drawCircle(rightX, lowerY, 10f, lineOrientationPaint);
            drawRoundRectPath(canvas, new RectF(centerWidth - dpTOpx(50f), lowerY - dpTOpx(16f), centerWidth + dpTOpx(50f), lowerY + dpTOpx(16f)),
                    dpTOpx(18f), true, true, true, true, lineOrientationPaint);


            drawRoundRectPath2(canvas, new RectF(rightX - dpTOpx(16f), centerHeight + dpTOpx(50f), rightX + dpTOpx(16f), centerHeight - dpTOpx(50f)),
                    dpTOpx(18f), true, true, true, true, lineOrientationPaint);
            String resultWidth = String.format("%.1f", getDistanceCmWidth()) + " cm";
            if (!isCmState) resultWidth = String.format("%.3f", getDistanceInWidth()) + " in";
            Paint.FontMetrics fontMetrics = textPaintResult.getFontMetrics();
            float textHeight = Math.abs(fontMetrics.top - fontMetrics.bottom);
            canvas.drawText(resultWidth, centerWidth, lowerY + textHeight / 4, textPaintResult);

            canvas.save();
            canvas.rotate(-90, rightX, centerHeight);
            String resultHeight = String.format("%.1f", getDistanceCm()) + " cm";
            if (!isCmState) resultHeight = String.format("%.3f", getDistanceIn()) + " in";
            canvas.drawText(resultHeight, rightX, centerHeight + textHeight / 4, textPaintResult);
            canvas.restore();
        }
        String cm = "cm";
        String inch = "inch";

        if (isCmState) {
            canvas.drawText(cm, 15f, textPaintCm.measureText(cm) * 0.75f, textPaintCm);
            drawMarksInCm(canvas, grayPaintReplace);
            drawMarksInCm2(canvas, grayPaintReplace);
        } else {
            canvas.drawText(inch, 15f, textPaintCm.measureText(cm) * 0.75f, textPaintCm);
            drawMarksInInch(canvas, grayPaintReplace);
            drawMarksInInch2(canvas, grayPaintReplace);
        }
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

    public void drawRoundRectPath2(Canvas canvas, RectF rectF, float radius,
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
            path.lineTo(rectF.left, rectF.top - radius);
            path.quadTo(rectF.left, rectF.top, rectF.left + radius, rectF.top);
        } else {
            path.lineTo(rectF.left, rectF.top);
        }

        if (topRight) {
            path.lineTo(rectF.right - radius, rectF.top);
            path.quadTo(rectF.right, rectF.top, rectF.right, rectF.top - radius);
        } else {
            path.lineTo(rectF.right, rectF.top);
        }

        if (bottomRight) {
            path.lineTo(rectF.right, rectF.bottom + radius);
            path.quadTo(rectF.right, rectF.bottom, rectF.right - radius, rectF.bottom);
        } else {
            path.lineTo(rectF.right, rectF.bottom);
        }

        if (bottomLeft) {
            path.lineTo(rectF.left + radius, rectF.bottom);
            path.quadTo(rectF.left, rectF.bottom, rectF.left, rectF.bottom + radius);
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
                if (i != 0)
                    canvas.drawText(text, markWidth + dpTOpx(16), y + dpTOpx(6), textPaint);

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

    private void drawMarksInCm2(Canvas canvas, Paint paint) {
        float oneMmInPx = RulerUnit.mmToPx(1f, coefficient, getResources().getDisplayMetrics());

        for (int i = 0; i <= 1000; i++) {
            float x = oneMmInPx * i;
            float markWidth;

            if (i % 10 == 0) {
                markWidth = markCmWidth;
                String text = String.valueOf(i / 10);
                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                float textHeight = fontMetrics.bottom - fontMetrics.top;
                float textY = offsetTop + textHeight + markWidth;
                if (i != 10 && i != 0)
                    canvas.drawText(text, x, textY, textPaint);
            } else if (i % 5 == 0) {
                markWidth = markHalfCmWidth;
            } else {
                markWidth = markMmWidth;
            }

            canvas.drawLine(x, 0f + offsetTop, x, markWidth + offsetTop, paint);

            if (x >= getWidth()) {
                break;
            }
        }
    }

    private void drawMarksInInch(Canvas canvas, Paint paint) {
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
                    String text = String.valueOf(i);
                    if (i != 0)
                        canvas.drawText(text, markWidth + dpTOpx(16), y + dpTOpx(6), textPaint);
                } else if (j == subdivisions / 2) {
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
    }

    private void drawMarksInInch2(Canvas canvas, Paint paint) {
        float oneInchInPx = RulerUnit.inToPx(1f, coefficient, getResources().getDisplayMetrics());
        float offset = RulerUnit.mmToPx(5f, coefficient, getResources().getDisplayMetrics()); // 0.5 cm in px

        int totalInches = 100;
        int subdivisions = 10;

        for (int i = 0; i <= totalInches; i++) {
            for (int j = 0; j < subdivisions; j++) {
                float x = oneInchInPx * i + (oneInchInPx / subdivisions) * j;
                float markWidth;
                if (j == 0) {
                    markWidth = markCmWidth;
                    String text = String.valueOf(i);
                    Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                    float textHeight = fontMetrics.bottom - fontMetrics.top;
                    float textY = offset + textHeight + markWidth;
                    if (i != 0)
                        canvas.drawText(text, x, textY, textPaint);
                } else if (j == subdivisions / 2) {
                    markWidth = markHalfCmWidth;
                } else {
                    markWidth = markMmWidth;
                }

                canvas.drawLine(x, 0f + offset, x, markWidth + offsetTop, paint);

                if (x >= getWidth()) {
                    break;
                }
            }
        }
    }

    public static double calculateDistance(float x1, float y1, float x2, float y2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pointerY = event.getY();
                pointerX = event.getX();
                pointer.set(pointerX, pointerY);
                PointF x1x2 = new PointF((x1.x + x2.x) / 2, (x1.y + x2.y) / 2);
                PointF x1x3 = new PointF((x1.x + x3.x) / 2, (x1.y + x3.y) / 2);
                PointF x2x4 = new PointF((x2.x + x4.x) / 2, (x2.y + x4.y) / 2);
                PointF x3x4 = new PointF((x3.x + x4.x) / 2, (x3.y + x4.y) / 2);
                if (isDraging) {
                    double pTox1x2 = calculateDistance(pointer.x, pointer.y, x1x2.x, x1x2.y);
                    double pTox1x3 = calculateDistance(pointer.x, pointer.y, x1x3.x, x1x3.y);
                    double pTox2x4 = calculateDistance(pointer.x, pointer.y, x2x4.x, x2x4.y);
                    double pTox3x4 = calculateDistance(pointer.x, pointer.y, x3x4.x, x3x4.y);
                    double min = Math.min(Math.min(pTox1x2, pTox1x3), Math.min(pTox2x4, pTox3x4));
                    if (min == pTox1x2) {
                        currentSection = UPPER_SECTION_VERTICAL;
                        if (min == pTox1x2 && min == pTox3x4) {
                            if (pointerY >= lowerY) currentSection = LOWER_SECTION_VERTICAL;
                            if (pointerY <= upperY) currentSection = UPPER_SECTION_VERTICAL;
                        }
                    } else if (min == pTox1x3) {
                        currentSection = LEFT_SECTION_HORIZONTAL;
                        if (min == pTox1x3 && min == pTox2x4) {
                            if (pointerX >= rightX) currentSection = RIGHT_SECTION_HORIZONTAL;
                            if (pointerX <= leftX) currentSection = LEFT_SECTION_HORIZONTAL;
                        }
                    } else if (min == pTox2x4) {
                        currentSection = RIGHT_SECTION_HORIZONTAL;
                    } else {
                        currentSection = LOWER_SECTION_VERTICAL;
                    }
                } else currentSection = LOWER_SECTION_VERTICAL;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDraging) {
                    float dy = event.getY() - pointerY;
                    float dx = event.getX() - pointerX;
                    switch (currentSection) {
                        case UPPER_SECTION_VERTICAL:
                            upperY += dy;
                            upperY = Math.max(0f, Math.min(lowerY - minDistance, upperY));
                            break;
                        case LOWER_SECTION_VERTICAL:
                            lowerY += dy;
                            lowerY = Math.max(upperY + minDistance, Math.min(getHeight(), lowerY));
                            break;
                        case LEFT_SECTION_HORIZONTAL:
                            leftX += dx;
                            leftX = Math.max(0f, Math.min(rightX - minDistance, leftX));
                            break;
                        case RIGHT_SECTION_HORIZONTAL:
                            rightX += dx;
                            rightX = Math.max(leftX + minDistance, Math.min(getWidth(), rightX));
                            break;
                    }
                    pointerY = event.getY();
                    pointerX = event.getX();
                    if (upperY < offsetTop) {
                        upperY = offsetTop;
                    }
                    if (lowerY > height) {
                        lowerY = height;
                    }
                    if (lowerY < offsetTop) {
                        lowerY = offsetTop;
                    }
                    if (leftX > width) leftX = width;
                    notifyDistanceChangeListener();
                    invalidate();
                } else {
                    float dy = event.getY() - pointerY;
                    float dx = event.getX() - pointerX;
                    lowerY += dy;
                    lowerY = Math.max(upperY + minDistance, Math.min(getHeight(), lowerY));
                    rightX += dx;
                    rightX = Math.max(leftX + minDistance, Math.min(getWidth(), rightX));
                    pointerY = event.getY();
                    pointerX = event.getX();
                    if (lowerY > height) {
                        lowerY = height;
                    }
                    if (lowerY < offsetTop) {
                        lowerY = offsetTop;
                    }
                    if (leftX > width) leftX = width;
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

    public float getDistanceCmWidth() {
        float distanceInInches = RulerUnit.pxToIn(Math.abs(rightX - leftX), coefficient, getResources().getDisplayMetrics());
        if (Objects.requireNonNull(unit) == RulerUnit.CM) {
            return distanceInInches * 2.54f;
        }
        return distanceInInches;
    }

    public float getDistanceIn() {
        return RulerUnit.pxToIn(Math.abs(upperY - lowerY), coefficient, getResources().getDisplayMetrics());
    }

    public float getDistanceInWidth() {
        return RulerUnit.pxToIn(Math.abs(rightX - leftX), coefficient, getResources().getDisplayMetrics());
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
