package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.home.protractor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;


public class ProtractorViewNull extends View {
    private Bitmap bitmapProtractor = null;
    private Bitmap scaledBitmap = null;


    public ProtractorViewNull(Context context) {
        super(context);
        init();
    }


    public ProtractorViewNull(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProtractorViewNull(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface ProtractorChanged {
        void onChanged(float angle);
    }

    private ProtractorChanged protractorChanged;

    public void setProtractorChanged(ProtractorChanged protractorChanged) {
        this.protractorChanged = protractorChanged;
    }

    private boolean isProtractorChanged=false;

    public void viewInDrag(boolean drag) {
        isProtractorChanged = drag;
        invalidate();
    }

    private Paint paintLine = new Paint();
    private Point touchPointLine1 = new Point(100, 100);
    private Point touchPointLine2 = new Point(100, 100);
    private Point centerPoint = new Point(2, 0);

    private void init() {
        bitmapProtractor = getBitmapFromDrawable(R.drawable.img_potractor_view_null);
        paintLine.setColor(Color.parseColor("#F95F00"));
        paintLine.setStrokeWidth(dpTOpx(3));
        paintLine.setAntiAlias(true);
    }

    private Bitmap getBitmapFromDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private int touchLine = -1;

    private float dpTOpx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }



    private float calculateAngleBetweenLines(Point center, Point p1, Point p2) {
        float dx1 = p1.x - center.x;
        float dy1 = p1.y - center.y;
        float dx2 = p2.x - center.x;
        float dy2 = p2.y - center.y;

        float dotProduct = dx1 * dx2 + dy1 * dy2;
        float magnitude1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        float magnitude2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        float cosTheta = dotProduct / (magnitude1 * magnitude2);
        float angle = (float) Math.acos(cosTheta);
        return (float) Math.toDegrees(angle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmapProtractor != null) {
            int newWidth = (int) (w * 0.8);
            int newHeight = (int) (newWidth * bitmapProtractor.getHeight() / bitmapProtractor.getWidth());
            scaledBitmap = Bitmap.createScaledBitmap(bitmapProtractor, newWidth, newHeight, true);

            if (scaledBitmap.getHeight() > h) {
                newHeight = (int) (h*0.9);
                newWidth = (int) (newHeight * scaledBitmap.getWidth() / (float)
                        scaledBitmap.getHeight());
                scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, newWidth, newHeight, true);
            }

            centerPoint.set(0, h / 2);
            touchPointLine1 = new Point(w, h / 2);
            touchPointLine2 = new Point(w, h / 2);
        }
    }

    float top = 0;

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if(isProtractorChanged){
            drawLineAngleLine1(canvas);
        }
        else {
            if (scaledBitmap != null) {
                top = (canvas.getHeight() - scaledBitmap.getHeight()) / 2f;
                canvas.drawBitmap(scaledBitmap, 2, top, new Paint(){{
                    setAntiAlias(true);
                }});


            }
            canvas.rotate(0);
            drawLineAngleLine1(canvas);
            drawLineAngleLine2(canvas);

            drawRoundRectPath(canvas, new RectF(2, centerPoint.y - dpTOpx(12),
                            dpTOpx(12), centerPoint.y + dpTOpx(12)), 30, false, true,
                    false, true, paintLine);
            float angle = calculateAngleBetweenLines(centerPoint, touchPointLine1, touchPointLine2);
            if (protractorChanged != null) {
                protractorChanged.onChanged(angle);
            }
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


    private float distanceFromPointToLine(Point lineStart, Point lineEnd, Point point) {
        float a = point.x - lineStart.x;
        float b = point.y - lineStart.y;
        float c = lineEnd.x - lineStart.x;
        float d = lineEnd.y - lineStart.y;

        float dot = a * c + b * d;
        float len_sq = c * c + d * d;
        float param = dot / len_sq;

        float xx, yy;

        if (param < 0 || (lineStart.x == lineEnd.x && lineStart.y == lineEnd.y)) {
            xx = lineStart.x;
            yy = lineStart.y;
        } else if (param > 1) {
            xx = lineEnd.x;
            yy = lineEnd.y;
        } else {
            xx = lineStart.x + param * c;
            yy = lineStart.y + param * d;
        }

        float dx = point.x - xx;
        float dy = point.y - yy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void drawLineAngleLine1(Canvas canvas) {
        float x1 = centerPoint.x+6;
        float y1 = centerPoint.y;
        float x2 = touchPointLine1.x;
        float y2 = touchPointLine1.y;
        float width = canvas.getWidth();
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float desiredLength = 0.9f * width;
        float ratio = desiredLength / distance;
        float endX = x1 + ratio * dx;
        float endY = y1 + ratio * dy;
        if(endX<=5){
            endX=5;
        }
        canvas.drawLine(x1, y1, endX, endY, paintLine);
        canvas.drawCircle(endX, endY, 20f, paintLine);
    }

    private void drawLineAngleLine2(Canvas canvas) {
        float x1 = centerPoint.x+6;
        float y1 = centerPoint.y;
        float x2 = touchPointLine2.x;
        float y2 = touchPointLine2.y;
        float width = canvas.getWidth();
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float desiredLength = 0.9f * width;
        float ratio = desiredLength / distance;
        float endX = x1 + ratio * dx;
        float endY = y1 + ratio * dy;
        if(endX<=5){
            endX=5;
        }
        canvas.drawLine(x1, y1, endX, endY, paintLine);
        canvas.drawCircle(endX, endY, 20f, paintLine);
    }

}
