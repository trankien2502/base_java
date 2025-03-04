package com.tkt.basejava.basejava1.basejava2.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.databinding.DialogExitAppBinding;
import com.tkt.basejava.basejava1.basejava2.databinding.LayoutFloatsingButtonBinding;
import com.tkt.basejava.basejava1.basejava2.databinding.PopupSelectActionBinding;
import com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon.IconStyle;
import com.tkt.basejava.basejava1.basejava2.ui.splash.SplashActivity;
import com.tkt.basejava.basejava1.basejava2.util.CheckUtils;
import com.tkt.basejava.basejava1.basejava2.util.SystemUtil;

public class ServiceScreen extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private View floatingView, menuView;
    private PopupSelectActionBinding menuBinding;
    private LayoutFloatsingButtonBinding floatingBinding;
    private WindowManager.LayoutParams params;
    private int screenWidth;
    private int screenHeight;
    private Handler handler;
    private boolean isMoving = false;
    private boolean isPress = false;
    private long touchStartTime;
    private long lastClickTime = 0;
    private boolean isLongPress = false;
    int countDouble = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        SystemUtil.setLocale(this);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "your_channel_id")
                .setContentTitle(getString(R.string.asisitive_touch_s_service_is_running))
                .setContentText(getString(R.string.tap_to_open))
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "your_channel_id",
                    "Foreground Service Assistive Touch",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public void setIconStyle(IconStyle iconStyle) {
        if (floatingView != null) windowManager.removeView(floatingView);
        floatingView = floatingBinding.getRoot();
        floatingBinding.floatingButton.setImageResource(iconStyle.getSource());
        windowManager.addView(floatingView, params);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        createNotificationChannel();
        startForeground(1, createNotification());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            screenWidth = bounds.width();
            screenHeight = bounds.height();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
        }
        // Khởi tạo WindowManager
        // Tạo LayoutParams cho View nổi
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // Tạo View nổi từ layout
        floatingBinding = LayoutFloatsingButtonBinding.inflate(LayoutInflater.from(this));
        floatingView = floatingBinding.getRoot();
        menuBinding = PopupSelectActionBinding.inflate(LayoutInflater.from(this));
        menuView = menuBinding.getRoot();
        // Xử lý sự kiện chạm kéo
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        touchStartTime = System.currentTimeMillis();
                        Log.e("check_service", "touchStartTime: " + touchStartTime);
                        isMoving = false;
                        isLongPress = false;
                        isPress = true;
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isPress) {
                                    if (!isMoving && (System.currentTimeMillis() - touchStartTime) > 500) {
                                        isLongPress = true;
                                        onFloatingIconLongPress();
                                        handler.removeCallbacks(this);
                                    } else {
                                        handler.postDelayed(this, 100);
//                                        Log.e("check_service", "pressing: ");
                                    }
                                } else {
                                    handler.removeCallbacks(this);
                                }
                            }
                        });
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs((event.getRawX() - initialTouchX)) > 10 || Math.abs((event.getRawY() - initialTouchY)) > 10) {
                            isMoving = true;
                            Log.e("check_service", "move");
                        }
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                        countDouble++;
                        if (!isMoving && !isLongPress) {
                            if (System.currentTimeMillis() - lastClickTime < 200 && countDouble == 2) {
                                onFloatingIconDoubleClick();
                                lastClickTime = 0;
                            } else {
                                lastClickTime = System.currentTimeMillis();
                                v.postDelayed(() -> {
                                    if (System.currentTimeMillis() - lastClickTime >= 200) {
                                        if (countDouble < 2)
                                            onFloatingIconClick();
                                        countDouble = 0;
                                    }
                                }, 200);
                            }
                        }
                        updatePositionAfterMove(floatingView, windowManager, params);
                        return true;
                }
                return false;
            }
        });
        menuBinding.llAction5.setOnClickListener(v -> {
            hidePopup();
        });
        menuBinding.llAction7.setOnClickListener(v -> {
            if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.e("check_service", "off");
            } else {
                if (ServiceControl.instance != null) {
                    ServiceControl service = new ServiceControl();
                    service.turnOffScreen();
                    Log.e("check_service", "on");
                } else {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "null");
                }
            }
            hidePopup();
        });

        // Thêm View nổi vào màn hình
        windowManager.addView(floatingView, params);
    }

    private void onFloatingIconClick() {
        showPopupChoose();
        Log.e("check_service", "click");
    }

    private void onFloatingIconDoubleClick() {
        Log.e("check_service", "2click");
        setIconStyle(new IconStyle(R.drawable.icon_12));
    }

    private void onFloatingIconLongPress() {
        Log.e("check_service", "longpress");
        setIconStyle(new IconStyle(R.drawable.icon_8));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

    @SuppressLint({"RestrictedApi", "ObjectAnimatorBinding"})
    private void smoothMoveView(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams params,
            int targetX,
            int targetY
    ) {
        ObjectAnimator animatorX = ObjectAnimator.ofInt(params, "x", params.x, targetX);
        animatorX.setDuration(300);
        animatorX.setInterpolator(new DecelerateInterpolator());
        animatorX.addUpdateListener(animation -> {
            params.x = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, params);
        });

        ObjectAnimator animatorY = ObjectAnimator.ofInt(params, "y", params.y, targetY);
        animatorY.setDuration(300);
        animatorY.setInterpolator(new DecelerateInterpolator());
        animatorY.addUpdateListener(animation -> {
            params.y = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, params);
        });

        animatorX.start();
        animatorY.start();
    }

    public void updatePositionAfterMove(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams params
    ) {
        int centerX = params.x + (view.getWidth() / 2);
        int centerY = params.y + (view.getHeight() / 2);

        int targetX;
        int targetY;

        if (centerX > screenWidth / 2 && centerY > screenHeight / 2) { // Bottom-right quadrant
            if (screenHeight - centerY <= screenWidth - centerX) {
                targetX = params.x;
                targetY = screenHeight - 4;
            } else {
                targetX = screenWidth - 4;
                targetY = params.y;
            }
        } else if (centerX > screenWidth / 2 && centerY <= screenHeight / 2) {
            if (screenWidth - centerX <= centerY) {
                targetX = screenWidth - 4;
                targetY = params.y;
            } else {
                targetX = params.x;
                targetY = 4;
            }
        } else if (centerX <= screenWidth / 2 && centerY > screenHeight / 2) {
            if (screenHeight - centerY <= centerX) {
                targetX = params.x;
                targetY = screenHeight - 4;
            } else {
                targetX = 4;
                targetY = params.y;
            }
        } else {
            if (centerX <= centerY) {
                targetX = 4;
                targetY = params.y;
            } else {
                targetX = params.x;
                targetY = 0;
            }
        }

        smoothMoveView(view, windowManager, params, targetX, targetY);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupChoose() {
        if (menuBinding == null) {
            menuBinding = PopupSelectActionBinding.inflate(LayoutInflater.from(this));
        }
        if (menuBinding.getRoot().getParent() == null) {  // Check if it's already added
            WindowManager.LayoutParams popupParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            popupParams.gravity = Gravity.CENTER;

            overlayView = new View(this);
            WindowManager.LayoutParams overlayParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            overlayParams.gravity = Gravity.CENTER;
            overlayView.setLayoutParams(overlayParams);

            overlayView.setOnTouchListener((v, event) -> {
                hidePopup();
                return true;
            });

            try {
                windowManager.addView(overlayView, overlayParams);
                windowManager.addView(menuBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hidePopup();
        }
    }

    private void hidePopup() {
        try {
            if (menuBinding != null && menuBinding.getRoot().getParent() != null) {
                windowManager.removeView(menuBinding.getRoot());
            }
            if (overlayView != null && overlayView.getParent() != null) {
                windowManager.removeView(overlayView);
            }
            overlayView = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
