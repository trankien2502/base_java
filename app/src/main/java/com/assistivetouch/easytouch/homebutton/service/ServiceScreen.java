package com.assistivetouch.easytouch.homebutton.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.accessibilityservice.AccessibilityService;
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
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
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

import androidx.core.app.NotificationCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.LayoutFloatsingButtonBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectAction2Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectActionBinding;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.ui.splash.SplashActivity;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

import java.util.ArrayList;

public class ServiceScreen extends Service {

    @SuppressLint("StaticFieldLeak")
    public static ServiceScreen instance;
    private WindowManager windowManager;
    ArrayList<ItemFunctionIcon> listMenu1 = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listMenu2 = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listFunctionCustomMenu = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listFunctionFloatingIcon = new ArrayList<>();

    private View overlayView;
    private View overlayView2;
    private View overlayViewPermission;
    private View floatingView;
    private PopupSelectActionBinding menuBinding;
    private PopupSelectAction2Binding menu2Binding;
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

    public void setIconStyle(int iconStyle) {
        if (floatingView != null) windowManager.removeView(floatingView);
        floatingView = floatingBinding.getRoot();
        floatingBinding.floatingButton.setImageResource(iconStyle);
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
        instance = this;
        listFunctionCustomMenu = SPUtils.getListCustomMenu();
        listFunctionFloatingIcon = SPUtils.getListFloatingIcon();
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
                        if (Math.abs((event.getRawX() - initialTouchX)) > 25f || Math.abs((event.getRawY() - initialTouchY)) > 25f) {
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
        // Thêm View nổi vào màn hình
        windowManager.addView(floatingView, params);
        setIconStyle(SPUtils.getInt(this, SPUtils.ICON_STYLE, R.drawable.icon_1));
    }

    private void onFloatingIconClick() {
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_SINGLE_TAP, listFunctionFloatingIcon.get(3));
        onActionDone(icon.getActionNumber());
        Log.e("check_service", "click");
    }

    private void onFloatingIconDoubleClick() {
        Log.e("check_service", "2click");
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_DOUBLE_TAP, listFunctionFloatingIcon.get(0));
        onActionDone(icon.getActionNumber());
        setIconStyle(R.drawable.icon_12);
    }

    private void onFloatingIconLongPress() {
        Log.e("check_service", "longpress");
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_LONG_PRESS, listFunctionFloatingIcon.get(0));
        onActionDone(icon.getActionNumber());
        setIconStyle(R.drawable.icon_8);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
        instance = null;
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
            menuBinding.llAction1.setStrokeWidth(0);
            menuBinding.llAction2.setStrokeWidth(0);
            menuBinding.llAction3.setStrokeWidth(0);
            menuBinding.llAction5.setStrokeWidth(0);
            menuBinding.llAction6.setStrokeWidth(0);
            menuBinding.llAction7.setStrokeWidth(0);
            menuBinding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            listMenu1 = SPUtils.getList(this, SPUtils.MENU_FUNCTION_1, SPUtils.getListDefaultMenu1());
            if (listMenu1 != null && !listMenu1.isEmpty()) {
                Log.e("menu_check", "menu2 start restore");
                menuBinding.imgAction1.setImageResource(listMenu1.get(0).getIconShow());
                menuBinding.txtAction1.setText(listMenu1.get(0).getText());
                menuBinding.imgAction2.setImageResource(listMenu1.get(1).getIconShow());
                menuBinding.txtAction2.setText(listMenu1.get(1).getText());
                menuBinding.imgAction3.setImageResource(listMenu1.get(2).getIconShow());
                menuBinding.txtAction3.setText(listMenu1.get(2).getText());
                menuBinding.imgAction5.setImageResource(listMenu1.get(3).getIconShow());
                menuBinding.txtAction5.setText(listMenu1.get(3).getText());
                menuBinding.imgAction6.setImageResource(listMenu1.get(4).getIconShow());
                menuBinding.txtAction6.setText(listMenu1.get(4).getText());
                menuBinding.imgAction7.setImageResource(listMenu1.get(5).getIconShow());
                menuBinding.txtAction7.setText(listMenu1.get(5).getText());
            }
            menuBinding.llAction1.setOnClickListener(v -> {
                onActionDone(listMenu1.get(0).getActionNumber());
                hidePopup();
            });
            menuBinding.llAction2.setOnClickListener(v -> {
                onActionDone(listMenu1.get(1).getActionNumber());
                hidePopup();
            });
            menuBinding.llAction3.setOnClickListener(v -> {
                onActionDone(listMenu1.get(2).getActionNumber());
                hidePopup();
            });
            menuBinding.llAction5.setOnClickListener(v -> {
                onActionDone(listMenu1.get(3).getActionNumber());
                hidePopup();
            });
            menuBinding.llAction6.setOnClickListener(v -> {
                onActionDone(listMenu1.get(4).getActionNumber());
                hidePopup();
            });
            menuBinding.llAction7.setOnClickListener(v -> {
                onActionDone(listMenu1.get(5).getActionNumber());
                hidePopup();
            });
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
    @SuppressLint("ClickableViewAccessibility")
    private void showPopupDevice() {
        if (menu2Binding == null) {
            menu2Binding = PopupSelectAction2Binding.inflate(LayoutInflater.from(this));
        }
        if (menu2Binding.getRoot().getParent() == null) {  // Check if it's already added
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
            menu2Binding.llAction1.setStrokeWidth(0);
            menu2Binding.llAction2.setStrokeWidth(0);
            menu2Binding.llAction3.setStrokeWidth(0);
            menu2Binding.llAction5.setStrokeWidth(0);
            menu2Binding.llAction6.setStrokeWidth(0);
            menu2Binding.llAction7.setStrokeWidth(0);
            menu2Binding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            listMenu2 = SPUtils.getList(this, SPUtils.MENU_FUNCTION_2, SPUtils.getListDefaultMenu2());
            if (listMenu2 != null && !listMenu2.isEmpty()) {
                Log.e("menu_check", "menu2 start restore");
                menu2Binding.imgAction1.setImageResource(listMenu2.get(0).getIconShow());
                menu2Binding.txtAction1.setText(listMenu2.get(0).getText());
                menu2Binding.imgAction2.setImageResource(listMenu2.get(1).getIconShow());
                menu2Binding.txtAction2.setText(listMenu2.get(1).getText());
                menu2Binding.imgAction3.setImageResource(listMenu2.get(2).getIconShow());
                menu2Binding.txtAction3.setText(listMenu2.get(2).getText());
                menu2Binding.imgAction5.setImageResource(listMenu2.get(3).getIconShow());
                menu2Binding.txtAction5.setText(listMenu2.get(3).getText());
                menu2Binding.imgAction6.setImageResource(listMenu2.get(4).getIconShow());
                menu2Binding.txtAction6.setText(listMenu2.get(4).getText());
                menu2Binding.imgAction7.setImageResource(listMenu2.get(5).getIconShow());
                menu2Binding.txtAction7.setText(listMenu2.get(5).getText());
            }
            menu2Binding.llAction1.setOnClickListener(v -> {
                onActionDone(listMenu2.get(0).getActionNumber());
            });

            menu2Binding.llAction2.setOnClickListener(v -> {
                onActionDone(listMenu2.get(1).getActionNumber());
                hidePopup2();
            });
            menu2Binding.llAction3.setOnClickListener(v -> {
                onActionDone(listMenu2.get(2).getActionNumber());
                hidePopup2();
            });
            menu2Binding.llAction4.setOnClickListener(v -> {
                showPopupChoose();
                hidePopup2();
            });
            menu2Binding.llAction5.setOnClickListener(v -> {
                onActionDone(listMenu2.get(3).getActionNumber());
                hidePopup2();
            });
            menu2Binding.llAction6.setOnClickListener(v -> {
                onActionDone(listMenu2.get(4).getActionNumber());
                hidePopup2();
            });
            menu2Binding.llAction7.setOnClickListener(v -> {
                onActionDone(listMenu2.get(5).getActionNumber());
                hidePopup2();
            });
            overlayView2 = new View(this);
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
            overlayView2.setLayoutParams(overlayParams);

            overlayView2.setOnTouchListener((v, event) -> {
                hidePopup2();
                return true;
            });

            try {
                windowManager.addView(overlayView2, overlayParams);
                windowManager.addView(menu2Binding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hidePopup2();
        }
    }

    private void hidePopup2() {
        try {
            if (menu2Binding != null && menu2Binding.getRoot().getParent() != null) {
                windowManager.removeView(menu2Binding.getRoot());
            }
            if (overlayView2 != null && overlayView2.getParent() != null) {
                windowManager.removeView(overlayView2);
            }
            overlayView2 = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onActionDone(int i) {
        switch (i) {
            case ItemFunctionIcon.ACTION_SCREEN_RECORDER:
                Log.d("action_check", "action: record video");
                break;
            case ItemFunctionIcon.ACTION_BLUETOOTH:
                Log.d("action_check", "action: bluetooth");
                break;
            case ItemFunctionIcon.ACTION_AIRPLANE:
                Intent intentAirplane = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                intentAirplane.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentAirplane);
                Log.d("action_check", "action: airplane");
                break;
            case ItemFunctionIcon.ACTION_LOCATION:
                Intent intentLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intentLocation.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLocation);
                Log.d("action_check", "action: location");
                break;
            case ItemFunctionIcon.ACTION_FLASHLIGHT:
                Log.d("action_check", "action: flashlight");
                break;
            case ItemFunctionIcon.ACTION_VOLUME_OPTION:
                Log.d("action_check", "action: volume");
                break;
            case ItemFunctionIcon.ACTION_TIME_OUT:
                Log.d("action_check", "action: time out");
                break;
            case ItemFunctionIcon.ACTION_ALL_APP:
                Log.d("action_check", "action: all app");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_ACCESSIBILITY_ALL_APPS);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_HOME:
                Log.d("action_check", "action: home");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_WIFI:
                Log.d("action_check", "action: wifi");
                break;
            case ItemFunctionIcon.ACTION_BRIGHTNESS:
                Log.d("action_check", "action: brightness");
                break;
            case ItemFunctionIcon.ACTION_DEVICE:
                showPopupDevice();
                Log.d("action_check", "action: device");
                break;
            case ItemFunctionIcon.ACTION_SCREEN_SHOT:
                Log.d("action_check", "action: screenshot");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_NOTIFICATION:
                Log.d("action_check", "action: notification");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_FAVOURITE:
                Log.d("action_check", "action: favourite");
                break;
            case ItemFunctionIcon.ACTION_RECENT:
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                Log.d("action_check", "action: recent");
                break;
            case ItemFunctionIcon.ACTION_LOCK_SCREEN:
                Log.d("action_check", "action: lockscreen");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_SETTINGS:
                Log.d("action_check", "action: all app");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                Log.d("action_check", "action: setting");
                break;
            case ItemFunctionIcon.ACTION_BACK:
                Log.d("action_check", "action: back");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_VOLUME_DOWN:
                Log.d("action_check", "action: volume down");
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                if (currentVolume > 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, AudioManager.FLAG_SHOW_UI);
                } else
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
                break;
            case ItemFunctionIcon.ACTION_NONE:
                Log.d("action_check", "action: none");
                break;
            case ItemFunctionIcon.ACTION_VOLUME_UP:
                AudioManager audioManagerUp = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManagerUp.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolumeUp = audioManagerUp.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolumeUp < maxVolume) {
                    audioManagerUp.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumeUp + 1, AudioManager.FLAG_SHOW_UI);
                } else
                    audioManagerUp.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
                Log.d("action_check", "action: volume up");
                break;
            case ItemFunctionIcon.ACTION_OPEN_MENU:
                showPopupChoose();
                Log.d("action_check", "action: open menu");
                break;
            case ItemFunctionIcon.ACTION_POWER:
                Log.d("action_check", "action: power");
                if (!CheckUtils.isAccessibilitySettingsOn(this, ServiceControl.class)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e("check_service", "off");
                } else {
                    if (ServiceControl.instance != null) {
                        ServiceControl.instance.performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
                        Log.e("check_service", "on");
                    } else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.e("check_service", "null");
                    }
                }
                break;
            case ItemFunctionIcon.ACTION_CAMERA:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCamera);
                Log.d("action_check", "action: camera");
                break;
            case ItemFunctionIcon.ACTION_LOCK_ROTATION:
                Log.d("action_check", "action: lock rotation");
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogPermissionAccessibility() {
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
            menuBinding.llAction1.setOnClickListener(v -> {
                onActionDone(listMenu1.get(0).getActionNumber());
                hideDialogPermission();
            });
            menuBinding.llAction2.setOnClickListener(v -> {
                onActionDone(listMenu1.get(1).getActionNumber());
                hideDialogPermission();
            });
            overlayViewPermission = new View(this);
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
            overlayViewPermission.setLayoutParams(overlayParams);

            overlayViewPermission.setOnTouchListener((v, event) -> {
                hideDialogPermission();
                return true;
            });

            try {
                windowManager.addView(overlayViewPermission, overlayParams);
                windowManager.addView(menuBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideDialogPermission();
        }
    }

    private void hideDialogPermission() {
        try {
            if (menuBinding != null && menuBinding.getRoot().getParent() != null) {
                windowManager.removeView(menuBinding.getRoot());
            }
            if (overlayViewPermission != null && overlayViewPermission.getParent() != null) {
                windowManager.removeView(overlayViewPermission);
            }
            overlayViewPermission = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
