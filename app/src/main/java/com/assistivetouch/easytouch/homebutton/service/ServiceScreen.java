package com.assistivetouch.easytouch.homebutton.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.media.AudioManager.FLAG_SHOW_UI;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.assistivetouch.easytouch.homebutton.MyApplication;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.LayoutFloatsingButtonBinding;
import com.assistivetouch.easytouch.homebutton.databinding.LayoutVolumeButtonBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupBrightnessBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectAction2Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectActionBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectFavouriteBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupTimeOutBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVolumeOptionBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig1Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig2Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig3Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig4Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig5Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig6Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig7Binding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig8Binding;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfo;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.ui.home.AllAppActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.ScreenRecorderActivity;
import com.assistivetouch.easytouch.homebutton.ui.home.ScreenshotActivity;
import com.assistivetouch.easytouch.homebutton.ui.screenshot.RecorderManager;
import com.assistivetouch.easytouch.homebutton.ui.screenshot.ScreenshotManager;
import com.assistivetouch.easytouch.homebutton.ui.screenshot.ScreenshotResult;
import com.assistivetouch.easytouch.homebutton.ui.setting.SettingActivity;
import com.assistivetouch.easytouch.homebutton.ui.splash.SplashActivity;
import com.assistivetouch.easytouch.homebutton.util.CheckUtils;
import com.assistivetouch.easytouch.homebutton.util.FlashlightProvider;
import com.assistivetouch.easytouch.homebutton.util.ImageUtils;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;
import com.assistivetouch.easytouch.homebutton.util.widget.UiLinearLayout;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ServiceScreen extends Service {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 101;
    @SuppressLint("StaticFieldLeak")
    public static ServiceScreen instance;
    private WindowManager windowManager;
    String filePath;
    ArrayList<ItemFunctionIcon> listMenu1 = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listMenu2 = new ArrayList<>();
    ArrayList<ItemAppInfo> listFavourite = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listFunctionCustomMenu = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listFunctionFloatingIcon = new ArrayList<>();
    FlashlightProvider flashlightProvider;

    private View overlayView, darknessView;
    private View overlayView2, overlayViewDialog;
    private View overlayViewPermission;
    public View floatingView;
    public View volumeView;
    private PopupSelectActionBinding menuBinding;
    private PopupSelectAction2Binding menu2Binding;
    private PopupTimeOutBinding timeOutBinding;
    private PopupBrightnessBinding brightnessBinding;
    private PopupVolumeOptionBinding volumeOptionBinding;
    private PopupSelectFavouriteBinding favouriteBinding;
    private LayoutFloatsingButtonBinding floatingBinding;
    private LayoutVolumeButtonBinding volumeBinding;
    private PopupVoulumeConfig1Binding menuVolume1Binding;
    private PopupVoulumeConfig2Binding menuVolume2Binding;
    private PopupVoulumeConfig3Binding menuVolume3Binding;
    private PopupVoulumeConfig4Binding menuVolume4Binding;
    private PopupVoulumeConfig5Binding menuVolume5Binding;
    private PopupVoulumeConfig6Binding menuVolume6Binding;
    private PopupVoulumeConfig7Binding menuVolume7Binding;
    private PopupVoulumeConfig8Binding menuVolume8Binding;
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams paramsVolume;
    private int screenWidth;
    private int screenHeight;

    private int screenDensity;

    private Handler handler;
    private boolean isMoving = false;
    private boolean isPress = false;
    private long touchStartTime;
    private long lastClickTime = 0;
    private boolean isLongPress = false;
    int countDouble = 0;
    private boolean isShowMenu1 = true;
    public RecorderManager recorderManager;
    ScreenshotManager screenshotManager;
    private static final int REQUEST_CODE = 1000;
    private MediaProjectionManager mediaProjectionManager;
    public MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;

    private boolean isRecord = false;
    private boolean isLockRotation = false;
    private boolean isFlashlightOn = false;
    private Uri mUri = null;
    private int edgeDistance = 0;
    int w, h;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("IS_ADD_TOUCH_ICON")) {
            boolean value = intent.getBooleanExtra("IS_ADD_TOUCH_ICON", false);
            if (value) addFloatingIcon();
            else addVolumeIcon();
        }
        return START_STICKY;
    }

    private Notification createNotification() {
        SystemUtil.setLocale(this);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(getString(R.string.asisitive_touch_s_service_is_running))
                .setContentText(getString(R.string.tap_to_open))
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
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

    public void dadDarknessView() {
        darknessView = new View(this);
        darknessView.setBackgroundColor(Color.parseColor("#66000000"));

        // Cấu hình LayoutParams cho Overlay
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );
        darknessView.setAlpha((float) SPUtils.getInt(this, SPUtils.DARK_PERCENT, 0) / 255);
        windowManager.addView(darknessView, params);
    }

    public void setDarknessLevel(int alpha) {
        if (darknessView != null) {
            darknessView.setAlpha((float) alpha / 255);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        flashlightProvider = new FlashlightProvider(this, new FlashlightProvider.FlashChangeResult() {
            @Override
            public void onChangeFlash(boolean z) {
                isFlashlightOn = z;
            }
        });
        recorderManager = new RecorderManager(this, new ScreenshotResult() {
            @Override
            public void onImageResult(Uri uri, boolean z) {

            }
        });
        listFunctionCustomMenu = SPUtils.getListCustomMenu();
        listFunctionFloatingIcon = SPUtils.getListFloatingIcon();
        handler = new Handler(Looper.getMainLooper());
        startForeground(1, createNotification());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            screenWidth = bounds.width();
            screenHeight = bounds.height();
            screenDensity = getResources().getDisplayMetrics().densityDpi;

        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
            screenDensity = displayMetrics.densityDpi;
        }
        SPUtils.putSize(this, new int[]{screenWidth, screenHeight, 0});

    }

    public void addFloatingIcon() {
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

    public void updateFloatingViewSize(int newSizeDp) {
        // Chuyển đổi từ dp sang px
        int newWidthPx = dpToPx(60) + dpToPx(newSizeDp);
        int newHeightPx = dpToPx(60) + dpToPx(newSizeDp);

        // Cập nhật kích thước
        paramsVolume.width = newWidthPx;
        paramsVolume.height = newHeightPx;

        // Cập nhật lại View nổi
        windowManager.updateViewLayout(volumeView, paramsVolume);
        updatePositionAfterMoveVolume(volumeView, windowManager, paramsVolume);
    }

    public int dpToPx(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void removeFloatingView() {
        if (floatingView != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }
    }

    public void addVolumeIcon() {
        volumeBinding = LayoutVolumeButtonBinding.inflate(LayoutInflater.from(this));
        volumeView = volumeBinding.getRoot();
        w = dpToPx(60f) + dpToPx(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_SIZE, 0));
        h = dpToPx(60f) + dpToPx(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_SIZE, 0));
        if (paramsVolume == null) {
            paramsVolume = new WindowManager.LayoutParams(
                    w,
                    h,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT
            );

            paramsVolume.gravity = Gravity.START | Gravity.TOP;
            paramsVolume.x = screenWidth - w + w * SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) / 100;
            paramsVolume.y = 100;  // Điều chỉnh vị trí theo chiều dọc
        }
        switch (SPUtils.getInt(this, SPUtils.VOLUME_STYLE_NUMBER, 1)) {
            case 1:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_1);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#3392FF"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_2);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#FF3336"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#000000"));
                break;
            case 3:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_3);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#3392FF"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 4:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_1);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#ffffff"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#3392FF"));
                break;
            case 5:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_5);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#ffffff"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#000000"));
                break;
            case 6:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_6);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#39BDFF"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 7:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_7);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#FBA65B"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case 8:
                volumeBinding.ivBorder.setImageResource(R.drawable.img_boder_8);
                volumeBinding.ivButon.setColorFilter(Color.parseColor("#ffffff"));
                volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(Color.parseColor("#000000"));
                break;
        }
        if (SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_COLOR, -1) != -1)
            volumeBinding.ivButon.setColorFilter(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_COLOR, -1));
        if (SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, -1) != -1)
            volumeBinding.ivButtonBackgroundColor.setCardBackgroundColor(SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_BACKGROUND_COLOR, -1));
        volumeView.setAlpha((float) SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_ALPHA, 255) / 255);
        volumeView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsVolume.x;
                        initialY = paramsVolume.y;
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
                                        onVolumeIconLongPress();
                                        handler.removeCallbacks(this);
                                    } else {
                                        handler.postDelayed(this, 100);
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
                        if (SPUtils.getBoolean(ServiceScreen.this, SPUtils.VOLUME_BUTTON_FIX_POSITION, false))
                            return false;
                        paramsVolume.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsVolume.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(volumeView, paramsVolume);
                        return true;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                        countDouble++;
                        if (!isMoving && !isLongPress) {
                            onVolumeIconClick();
                        }
                        updatePositionAfterMoveVolume(volumeView, windowManager, paramsVolume);
                        return true;
                }
                return false;
            }
        });
        // Thêm View nổi vào màn hình
        dadDarknessView();
        windowManager.addView(volumeView, paramsVolume);
    }

    public void removeVolumeView() {
        if (volumeView != null) {
            windowManager.removeView(volumeView);
            volumeView = null;
            if (darknessView != null) {
                windowManager.removeView(darknessView);
                darknessView = null;
            }
        }
    }

    private void onFloatingIconClick() {
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_SINGLE_TAP, listFunctionFloatingIcon.get(3));
        onActionDone(icon, null, null, false);
        Log.e("check_service", "click");
    }

    private void onFloatingIconDoubleClick() {
        Log.e("check_service", "2click");
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_DOUBLE_TAP, listFunctionFloatingIcon.get(0));
        onActionDone(icon, null, null, false);
    }

    private void onFloatingIconLongPress() {
        Log.e("check_service", "longpress");
        ItemFunctionIcon icon = SPUtils.getObject(this, SPUtils.FLOATING_ICON_LONG_PRESS, listFunctionFloatingIcon.get(0));
        onActionDone(icon, null, null, false);
    }

    private void onVolumeIconClick() {
        showPopupVolume(SPUtils.getInt(this, SPUtils.VOLUME_STYLE_NUMBER, 1));
        Log.e("check_service", "clickvolume");
    }

    private void onVolumeIconLongPress() {
        Log.e("check_service", "longpressvolume");
        switch (SPUtils.getInt(this, SPUtils.LONG_PRESS_VOLUME_ACTION, 1)) {
            case 1:
                removeVolumeView();
                if (HomeActivity.instance != null) {
                    HomeActivity.instance.checkState();
                }
                break;
            case 2:
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
            case 3:
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
            case 4:
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, FLAG_SHOW_UI);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFloatingView();
        removeVolumeView();
        instance = null;
    }

    private void initViewAndBindViewVolumePopup(VerticalSeekBar sbMedia, View llMedia, VerticalSeekBar sbRing, View llRing, VerticalSeekBar sbNotification, View llNotification,
                                                VerticalSeekBar sbCall, View llCall, VerticalSeekBar sbBrightness, View llBright, VerticalSeekBar sbDark, View llDark) {
        boolean isShowMedia = SPUtils.getBoolean(this, SPUtils.SHOW_MEDIA, true);
        boolean isShowRing = SPUtils.getBoolean(this, SPUtils.SHOW_RINGTONE, true);
        boolean isShowNotification = SPUtils.getBoolean(this, SPUtils.SHOW_NOTIFICATION, true);
        boolean isShowCall = SPUtils.getBoolean(this, SPUtils.SHOW_CALL, true);
        boolean isShowBright = SPUtils.getBoolean(this, SPUtils.SHOW_BRIGHTNESS, false);
        boolean isShowDark = SPUtils.getBoolean(this, SPUtils.SHOW_DARKNESS, false);
        int darkPercent = SPUtils.getInt(this, SPUtils.DARK_PERCENT, 0);
        llMedia.setVisibility(isShowMedia ? VISIBLE : GONE);
        llRing.setVisibility(isShowRing ? VISIBLE : GONE);
        llNotification.setVisibility(isShowNotification ? VISIBLE : GONE);
        llCall.setVisibility(isShowCall ? VISIBLE : GONE);
        llBright.setVisibility(isShowBright ? VISIBLE : GONE);
        llDark.setVisibility(isShowDark ? VISIBLE : GONE);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int maxCallVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        int maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        sbMedia.setMaxValue(maxMediaVolume);
        sbNotification.setMaxValue(maxNotificationVolume);
        sbRing.setMaxValue(maxRingVolume);
        sbCall.setMaxValue(maxCallVolume);
        int mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int callVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int notificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        sbMedia.setProgress(mediaVolume);
        sbNotification.setProgress(notificationVolume);
        sbRing.setProgress(ringVolume);
        sbCall.setProgress(callVolume);
        sbDark.setProgress(darkPercent);
        try {
            ContentResolver contentResolver = getContentResolver();
            int bright = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            sbBrightness.setProgress(bright);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sbBrightness.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                try {
                    ContentResolver contentResolver = getContentResolver();
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, integer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        sbMedia.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, integer, 0);
                return null;
            }
        });
        sbRing.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                audioManager.setStreamVolume(AudioManager.STREAM_RING, integer, 0);
                return null;
            }
        });
        sbNotification.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, integer, 0);
                return null;
            }
        });
        sbCall.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, integer, 0);
                return null;
            }
        });
        sbDark.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                SPUtils.setInt(ServiceScreen.this, SPUtils.DARK_PERCENT, integer);
                setDarknessLevel(integer);
                return null;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupVolume(int i) {
        switch (i) {
            case 1:
                if (menuVolume1Binding == null) {
                    menuVolume1Binding = PopupVoulumeConfig1Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume1Binding.sbMedia, menuVolume1Binding.llMedia, menuVolume1Binding.sbRing, menuVolume1Binding.llRing, menuVolume1Binding.sbNotification, menuVolume1Binding.llNotification,
                        menuVolume1Binding.sbCall, menuVolume1Binding.llCall, menuVolume1Binding.sbBrightness, menuVolume1Binding.llBright, menuVolume1Binding.sbDark, menuVolume1Binding.llDark);
                addVolumeView(menuVolume1Binding.getRoot());
                break;
            case 2:
                if (menuVolume2Binding == null) {
                    menuVolume2Binding = PopupVoulumeConfig2Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume2Binding.sbMedia, menuVolume2Binding.llMedia, menuVolume2Binding.sbRing, menuVolume2Binding.llRing, menuVolume2Binding.sbNotification, menuVolume2Binding.llNotification,
                        menuVolume2Binding.sbCall, menuVolume2Binding.llCall, menuVolume2Binding.sbBrightness, menuVolume2Binding.llBright, menuVolume2Binding.sbDark, menuVolume2Binding.llDark);
                addVolumeView(menuVolume2Binding.getRoot());
                break;
            case 3:
                if (menuVolume3Binding == null) {
                    menuVolume3Binding = PopupVoulumeConfig3Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume3Binding.sbMedia, menuVolume3Binding.llMedia, menuVolume3Binding.sbRing, menuVolume3Binding.llRing, menuVolume3Binding.sbNotification, menuVolume3Binding.llNotification,
                        menuVolume3Binding.sbCall, menuVolume3Binding.llCall, menuVolume3Binding.sbBrightness, menuVolume3Binding.llBright, menuVolume3Binding.sbDark, menuVolume3Binding.llDark);
                addVolumeView(menuVolume3Binding.getRoot());
                break;
            case 4:
                if (menuVolume4Binding == null) {
                    menuVolume4Binding = PopupVoulumeConfig4Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume4Binding.sbMedia, menuVolume4Binding.llMedia, menuVolume4Binding.sbRing, menuVolume4Binding.llRing, menuVolume4Binding.sbNotification, menuVolume4Binding.llNotification,
                        menuVolume4Binding.sbCall, menuVolume4Binding.llCall, menuVolume4Binding.sbBrightness, menuVolume4Binding.llBright, menuVolume4Binding.sbDark, menuVolume4Binding.llDark);
                addVolumeView(menuVolume4Binding.getRoot());
                break;
            case 5:
                if (menuVolume5Binding == null) {
                    menuVolume5Binding = PopupVoulumeConfig5Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume5Binding.sbMedia, menuVolume5Binding.llMedia, menuVolume5Binding.sbRing, menuVolume5Binding.llRing, menuVolume5Binding.sbNotification, menuVolume5Binding.llNotification,
                        menuVolume5Binding.sbCall, menuVolume5Binding.llCall, menuVolume5Binding.sbBrightness, menuVolume5Binding.llBright, menuVolume5Binding.sbDark, menuVolume5Binding.llDark);
                addVolumeView(menuVolume5Binding.getRoot());
                break;
            case 6:
                if (menuVolume6Binding == null) {
                    menuVolume6Binding = PopupVoulumeConfig6Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume6Binding.sbMedia, menuVolume6Binding.llMedia, menuVolume6Binding.sbRing, menuVolume6Binding.llRing, menuVolume6Binding.sbNotification, menuVolume6Binding.llNotification,
                        menuVolume6Binding.sbCall, menuVolume6Binding.llCall, menuVolume6Binding.sbBrightness, menuVolume6Binding.llBright, menuVolume6Binding.sbDark, menuVolume6Binding.llDark);
                addVolumeView(menuVolume6Binding.getRoot());
                break;
            case 7:
                if (menuVolume7Binding == null) {
                    menuVolume7Binding = PopupVoulumeConfig7Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume7Binding.sbMedia, menuVolume7Binding.llMedia, menuVolume7Binding.sbRing, menuVolume7Binding.llRing, menuVolume7Binding.sbNotification, menuVolume7Binding.llNotification,
                        menuVolume7Binding.sbCall, menuVolume7Binding.llCall, menuVolume7Binding.sbBrightness, menuVolume7Binding.llBright, menuVolume7Binding.sbDark, menuVolume7Binding.llDark);
                addVolumeView(menuVolume7Binding.getRoot());
                break;
            case 8:
                if (menuVolume8Binding == null) {
                    menuVolume8Binding = PopupVoulumeConfig8Binding.inflate(LayoutInflater.from(this));
                }
                initViewAndBindViewVolumePopup(menuVolume8Binding.sbMedia, menuVolume8Binding.llMedia, menuVolume8Binding.sbRing, menuVolume8Binding.llRing, menuVolume8Binding.sbNotification, menuVolume8Binding.llNotification,
                        menuVolume8Binding.sbCall, menuVolume8Binding.llCall, menuVolume8Binding.sbBrightness, menuVolume8Binding.llBright, menuVolume8Binding.sbDark, menuVolume8Binding.llDark);
                addVolumeView(menuVolume8Binding.getRoot());
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addVolumeView(View view) {
        if (view.getParent() == null) {  // Check if it's already added
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
                windowManager.addView(view, popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hidePopup();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPopupChoose() {
        if (menuBinding == null) {
            menuBinding = PopupSelectActionBinding.inflate(LayoutInflater.from(this));

        }
        if (menuBinding.getRoot().getParent() == null) {  // Check if it's already added
            isShowMenu1 = true;
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
            for (ItemFunctionIcon icon : listMenu1) {
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_SCREEN_RECORDER) {
                    if (ScreenRecordService.instance != null) {
                        if (ScreenRecordService.instance.isRecord) {
                            Log.e("check_record", "instance isrecord");
                            icon.setIconShow(R.drawable.ic_action_recorder_on);
                            icon.setText(R.string.finish);
                        } else {
                            Log.e("check_record", "instance !isrecord");
                            icon.setIconShow(R.drawable.ic_action_video_recorder);
                            icon.setText(R.string.screen_recorder);
                        }
                    } else {
                        Log.e("check_record", "instance !isrecord");
                        icon.setIconShow(R.drawable.ic_action_video_recorder);
                        icon.setText(R.string.screen_recorder);
                    }
                }
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_FLASHLIGHT) {
                    if (!isFlashlightOn) {
                        icon.setIconShow(R.drawable.ic_action_flashlight);
                    } else {
                        icon.setIconShow(R.drawable.ic_action_flashlight_on);
                    }
                }
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_LOCK_ROTATION) {
                    if (isLockRotation) {
                        icon.setIconShow(R.drawable.ic_action_unlock_rotation);
                        icon.setText(R.string.unlock_rotation);
                    } else {
                        icon.setIconShow(R.drawable.ic_action_lock_rotation);
                        icon.setText(R.string.lock_rotation);
                    }

                }
            }
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
                onActionDone(listMenu1.get(0), menuBinding.imgAction1, menuBinding.txtAction1, true);
            });
            menuBinding.llAction2.setOnClickListener(v -> {
                onActionDone(listMenu1.get(1), menuBinding.imgAction2, menuBinding.txtAction2, true);
            });
            menuBinding.llAction3.setOnClickListener(v -> {
                onActionDone(listMenu1.get(2), menuBinding.imgAction3, menuBinding.txtAction3, true);
            });
            menuBinding.llAction5.setOnClickListener(v -> {
                onActionDone(listMenu1.get(3), menuBinding.imgAction5, menuBinding.txtAction5, true);
            });
            menuBinding.llAction6.setOnClickListener(v -> {
                onActionDone(listMenu1.get(4), menuBinding.imgAction6, menuBinding.txtAction6, true);
            });
            menuBinding.llAction7.setOnClickListener(v -> {
                onActionDone(listMenu1.get(5), menuBinding.imgAction7, menuBinding.txtAction7, true);
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
            if (menuVolume1Binding != null && menuVolume1Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume1Binding.getRoot());
            }
            if (menuVolume2Binding != null && menuVolume2Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume2Binding.getRoot());
            }
            if (menuVolume3Binding != null && menuVolume3Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume3Binding.getRoot());
            }
            if (menuVolume4Binding != null && menuVolume4Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume4Binding.getRoot());
            }
            if (menuVolume5Binding != null && menuVolume5Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume5Binding.getRoot());
            }
            if (menuVolume6Binding != null && menuVolume6Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume6Binding.getRoot());
            }
            if (menuVolume7Binding != null && menuVolume7Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume7Binding.getRoot());
            }
            if (menuVolume8Binding != null && menuVolume8Binding.getRoot().getParent() != null) {
                windowManager.removeView(menuVolume8Binding.getRoot());
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
            isShowMenu1 = false;
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
            for (ItemFunctionIcon icon : listMenu2) {
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_SCREEN_RECORDER) {
                    if (ScreenRecordService.instance != null) {
                        if (ScreenRecordService.instance.isRecord) {
                            Log.e("check_record", "instance isrecord");
                            icon.setIconShow(R.drawable.ic_action_recorder_on);
                            icon.setText(R.string.finish);
                        } else {
                            Log.e("check_record", "instance !isrecord");
                            icon.setIconShow(R.drawable.ic_action_video_recorder);
                            icon.setText(R.string.screen_recorder);
                        }
                    } else {
                        Log.e("check_record", "instance !isrecord");
                        icon.setIconShow(R.drawable.ic_action_video_recorder);
                        icon.setText(R.string.screen_recorder);
                    }

                }
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_FLASHLIGHT) {
                    if (!isFlashlightOn) {
                        icon.setIconShow(R.drawable.ic_action_flashlight);
                    } else {
                        icon.setIconShow(R.drawable.ic_action_flashlight_on);
                    }
                }
                if (icon.getActionNumber() == ItemFunctionIcon.ACTION_LOCK_ROTATION) {
                    if (isLockRotation) {
                        icon.setIconShow(R.drawable.ic_action_unlock_rotation);
                        icon.setText(R.string.unlock_rotation);
                    } else {
                        icon.setIconShow(R.drawable.ic_action_lock_rotation);
                        icon.setText(R.string.lock_rotation);
                    }

                }
            }
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
                onActionDone(listMenu2.get(0), menu2Binding.imgAction1, menu2Binding.txtAction1, true);
            });

            menu2Binding.llAction2.setOnClickListener(v -> {
                onActionDone(listMenu2.get(1), menu2Binding.imgAction2, menu2Binding.txtAction2, true);
            });
            menu2Binding.llAction3.setOnClickListener(v -> {
                onActionDone(listMenu2.get(2), menu2Binding.imgAction3, menu2Binding.txtAction3, true);
            });
            menu2Binding.llAction4.setOnClickListener(v -> {
                showPopupChoose();
                hidePopup2();
            });
            menu2Binding.llAction5.setOnClickListener(v -> {
                onActionDone(listMenu2.get(3), menu2Binding.imgAction5, menu2Binding.txtAction5, true);
            });
            menu2Binding.llAction6.setOnClickListener(v -> {
                onActionDone(listMenu2.get(4), menu2Binding.imgAction6, menu2Binding.txtAction6, true);
            });
            menu2Binding.llAction7.setOnClickListener(v -> {
                onActionDone(listMenu2.get(5), menu2Binding.imgAction7, menu2Binding.txtAction7, true);
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


    private void onActionDone(ItemFunctionIcon icon, ImageView view, TextView textView, boolean isChangeView) {
        switch (icon.getActionNumber()) {
            case ItemFunctionIcon.ACTION_SCREEN_RECORDER:
                Log.d("action_check", "action: record video");
                hidePopup();
                hidePopup2();
                if (ScreenRecordService.instance != null) {
                    if (ScreenRecordService.instance.isRecord) {
                        Log.e("check_record", "instance isrecord");
                        Intent serviceIntent = new Intent(this, ScreenRecordService.class);
                        stopService(serviceIntent); // Bắt đầu Service
                    } else {
                        Log.e("check_record", "instance !isrecord");
                        Intent intentVideo = new Intent(this, ScreenRecorderActivity.class);
                        intentVideo.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentVideo);
                    }
                } else {
                    Log.e("check_record", "instance !isrecord");
                    Intent intentVideo = new Intent(this, ScreenRecorderActivity.class);
                    intentVideo.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentVideo);
                }

                break;
            case ItemFunctionIcon.ACTION_BLUETOOTH:
                Log.d("action_check", "action: bluetooth");
                Intent intentBluetooth = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                intentBluetooth.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentBluetooth);
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_AIRPLANE:
                Intent intentAirplane = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                intentAirplane.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentAirplane);
                hidePopup();
                hidePopup2();
                Log.d("action_check", "action: airplane");
                break;
            case ItemFunctionIcon.ACTION_LOCATION:
                Intent intentLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intentLocation.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLocation);
                hidePopup();
                hidePopup2();
                Log.d("action_check", "action: location");
                break;
            case ItemFunctionIcon.ACTION_FLASHLIGHT:
                Log.d("action_check", "action: flashlight");
                if (this.flashlightProvider.isOn()) {
                    this.flashlightProvider.turnFlashlightOff();
                    if (isChangeView) view.setImageResource(R.drawable.ic_action_flashlight);
                } else {
                    this.flashlightProvider.turnFlashlightOn();
                    if (isChangeView) view.setImageResource(R.drawable.ic_action_flashlight_on);

                }
                break;
            case ItemFunctionIcon.ACTION_VOLUME_OPTION:
                Log.d("action_check", "action: volume");
                hidePopup();
                hidePopup2();
                showVolumeOption();
                break;
            case ItemFunctionIcon.ACTION_TIME_OUT:
                Log.d("action_check", "action: time out");
                hidePopup();
                hidePopup2();
                showDialogTimeOut();
                break;
            case ItemFunctionIcon.ACTION_ALL_APP:
                Log.d("action_check", "action: all app");
                hidePopup();
                hidePopup2();
                showAllApp();
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
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_WIFI:
                Log.d("action_check", "action: wifi");
                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intentWifi.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentWifi);
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_BRIGHTNESS:
                Log.d("action_check", "action: brightness");
                hidePopup();
                hidePopup2();
                showDialogBrightness();
                break;
            case ItemFunctionIcon.ACTION_DEVICE:
                hidePopup();
                hidePopup2();
                showPopupDevice();
                Log.d("action_check", "action: device");
                break;
            case ItemFunctionIcon.ACTION_SCREEN_SHOT:
                Log.d("action_check", "action: screenshot");
                hidePopup();
                hidePopup2();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
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
                } else {
                    Intent intentVideo = new Intent(this, ScreenshotActivity.class);
                    intentVideo.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentVideo);
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
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_FAVOURITE:
                Log.d("action_check", "action: favourite");
                hidePopup();
                hidePopup2();
                showFavourite();
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
                hidePopup();
                hidePopup2();
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
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_SETTINGS:
                Intent intentSetting = new Intent();
                intentSetting.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intentSetting.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intentSetting.setData(uri);
                startActivity(intentSetting);
                hidePopup();
                hidePopup2();
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
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_VOLUME_DOWN:
                Log.d("action_check", "action: volume down");
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                if (currentVolume > 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, FLAG_SHOW_UI);
                } else
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, FLAG_SHOW_UI);
                break;
            case ItemFunctionIcon.ACTION_NONE:
                Log.d("action_check", "action: none");
                break;
            case ItemFunctionIcon.ACTION_VOLUME_UP:
                AudioManager audioManagerUp = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManagerUp.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolumeUp = audioManagerUp.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolumeUp < maxVolume) {
                    audioManagerUp.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumeUp + 1, FLAG_SHOW_UI);
                } else
                    audioManagerUp.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, FLAG_SHOW_UI);
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
                hidePopup();
                hidePopup2();
                break;
            case ItemFunctionIcon.ACTION_CAMERA:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCamera);
                hidePopup();
                hidePopup2();
                Log.d("action_check", "action: camera");
                break;
            case ItemFunctionIcon.ACTION_LOCK_ROTATION:
                Log.d("action_check", "action: lock rotation");
                try {
                    ContentResolver contentResolver = getContentResolver();
                    int rotation = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION);
                    if (rotation == 1) {
                        isLockRotation = true;
                        Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0); //khoa xoay man hinh
                        view.setImageResource(R.drawable.ic_action_unlock_rotation);
                        textView.setText(R.string.unlock_rotation);
                        icon.setIconShow(R.drawable.ic_action_unlock_rotation);
                        icon.setText(R.string.unlock_rotation);
                    } else {
                        isLockRotation = false;
                        Settings.System.putInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 1);
                        view.setImageResource(R.drawable.ic_action_lock_rotation);
                        textView.setText(R.string.lock_rotation);
                        icon.setIconShow(R.drawable.ic_action_lock_rotation);
                        icon.setText(R.string.lock_rotation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void makePath() {
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "RecordScreen";
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, (int) R.string.error_sd, Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(str);
        if (file.exists() ? true : file.mkdir()) {
            filePath = str + File.separator + "video_" + System.currentTimeMillis() + ".mp4";
            return;
        }
        Toast.makeText(this, (int) R.string.error_record, Toast.LENGTH_SHORT).show();
    }

    public void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        if (PermissionManager.checkMicrophonePermission(this)) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoSize(screenWidth, screenHeight);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        if (PermissionManager.checkMicrophonePermission(this)) {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }
        mediaRecorder.setVideoFrameRate(60);     // 60 FPS
        mediaRecorder.setVideoEncodingBitRate(8 * 1000 * 1000);
        if (Build.VERSION.SDK_INT < 29) {
            makePath();
        } else {
            String str = "video_" + System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("relative_path", Environment.DIRECTORY_MOVIES + File.separator + "RecordScreen");
            contentValues.put("title", str);
            contentValues.put("_display_name", str);
            contentValues.put("mime_type", "video/mp4");
            contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            this.mUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        if (this.mUri == null) {
            this.mediaRecorder.setOutputFile(this.filePath);
        } else {
            try {
                FileDescriptor fileDescriptor = getContentResolver().openFileDescriptor(this.mUri, "rw").getFileDescriptor();
                if (fileDescriptor != null) {
                    this.mediaRecorder.setOutputFile(fileDescriptor);
                } else {
                    makePath();
                    this.mediaRecorder.setOutputFile(this.filePath);
                }
            } catch (Exception unused2) {
                makePath();
                this.mediaRecorder.setOutputFile(this.filePath);
            }
        }


        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("check_record", "error: ", e);
        }
    }

    public void startRecording() {
        isRecord = true;
        Log.e("check_record", "record!");
        virtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecorder",
                screenWidth, screenHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);

        mediaRecorder.start();
    }

    public void stopRecording() {
        isRecord = false;
        Log.e("check_record", "stop!");
        if (mediaRecorder != null) {
            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            mediaRecorder.stop();
            mediaRecorder.reset();
        }
        if (virtualDisplay != null) {
            virtualDisplay.release();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogBrightness() {
        if (brightnessBinding == null) {
            brightnessBinding = PopupBrightnessBinding.inflate(LayoutInflater.from(this));
        }
        if (brightnessBinding.getRoot().getParent() == null) {  // Check if it's already added
            WindowManager.LayoutParams popupParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            brightnessBinding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            popupParams.gravity = Gravity.CENTER;
            int bright = 0, mode = 0;
            try {
                ContentResolver contentResolver = getContentResolver();
                bright = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
                mode = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
                brightnessBinding.sbAlpha.setProgress(bright);
                if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    brightnessBinding.ivAutoBright.setImageResource(R.drawable.brightness_s);
                } else {
                    brightnessBinding.ivAutoBright.setImageResource(R.drawable.brightness_sn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            brightnessBinding.ivBack.setOnClickListener(v -> {
                hideDialog(true);
            });
            brightnessBinding.ivAutoBright.setOnClickListener(v -> {
                try {
                    ContentResolver contentResolver = getContentResolver();
                    if (Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                        brightnessBinding.ivAutoBright.setImageResource(R.drawable.brightness_sn);
                    } else {
                        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                        brightnessBinding.ivAutoBright.setImageResource(R.drawable.brightness_s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            brightnessBinding.sbAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    try {
                        ContentResolver contentResolver = getContentResolver();
                        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            overlayViewDialog = new View(this);
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
            overlayViewDialog.setLayoutParams(overlayParams);

            overlayViewDialog.setOnTouchListener((v, event) -> {
                hideDialog(false);
                return true;
            });

            try {
                windowManager.addView(overlayViewDialog, overlayParams);
                windowManager.addView(brightnessBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideDialog(false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showFavourite() {
        if (favouriteBinding == null) {
            favouriteBinding = PopupSelectFavouriteBinding.inflate(LayoutInflater.from(this));
        }
        if (favouriteBinding.getRoot().getParent() == null) {  // Check if it's already added
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
            favouriteBinding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            listFavourite = SPUtils.getListFavourite(this, SPUtils.FAVOURITE_APP, SPUtils.getListDefaultFavourite(this));
            if (listFavourite != null && !listFavourite.isEmpty()) {
                Log.e("menu_check", "menu2 start restore");
                if (listFavourite.get(0).getPackageName() != null) {
                    favouriteBinding.llAction1.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction1, listFavourite.get(0));
                }
                if (listFavourite.get(1).getPackageName() != null) {
                    favouriteBinding.llAction2.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction2, listFavourite.get(1));
                }
                if (listFavourite.get(2).getPackageName() != null) {
                    favouriteBinding.llAction3.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction3, listFavourite.get(2));
                }
                if (listFavourite.get(3).getPackageName() != null) {
                    favouriteBinding.llAction4.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction4, listFavourite.get(3));
                }
                if (listFavourite.get(4).getPackageName() != null) {
                    favouriteBinding.llAction6.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction6, listFavourite.get(4));
                }
                if (listFavourite.get(5).getPackageName() != null) {
                    favouriteBinding.llAction7.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction7, listFavourite.get(5));
                }
                if (listFavourite.get(6).getPackageName() != null) {
                    favouriteBinding.llAction8.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction8, listFavourite.get(6));
                }
                if (listFavourite.get(7).getPackageName() != null) {
                    favouriteBinding.llAction9.setStrokeWidth(0);
                    setImageApp(favouriteBinding.imgAction9, listFavourite.get(7));
                }
                favouriteBinding.llAction1.setOnClickListener(v -> {
                    checkApp(listFavourite.get(0), 0);
                });
                favouriteBinding.llAction2.setOnClickListener(v -> {
                    checkApp(listFavourite.get(1), 1);
                });
                favouriteBinding.llAction3.setOnClickListener(v -> {
                    checkApp(listFavourite.get(2), 2);
                });
                favouriteBinding.llAction4.setOnClickListener(v -> {
                    checkApp(listFavourite.get(3), 3);
                });
                favouriteBinding.llAction5.setOnClickListener(v -> {
                    hideDialog(true);
                });
                favouriteBinding.llAction6.setOnClickListener(v -> {
                    checkApp(listFavourite.get(4), 4);
                });
                favouriteBinding.llAction7.setOnClickListener(v -> {
                    checkApp(listFavourite.get(5), 5);
                });
                favouriteBinding.llAction8.setOnClickListener(v -> {
                    checkApp(listFavourite.get(6), 6);
                });
                favouriteBinding.llAction9.setOnClickListener(v -> {
                    checkApp(listFavourite.get(7), 7);
                });
            }

            overlayViewDialog = new View(this);
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
            overlayViewDialog.setLayoutParams(overlayParams);
            overlayViewDialog.setOnTouchListener((v, event) -> {
                hideDialog(false);
                return true;
            });

            try {
                windowManager.addView(overlayViewDialog, overlayParams);
                windowManager.addView(favouriteBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideDialog(false);
        }
    }

    private void setImageApp(ImageView imageView, ItemAppInfo appInfo) {
        try {
            Drawable drawable = getPackageManager().getApplicationIcon(appInfo.getPackageName());
            imageView.setImageDrawable(drawable);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkApp(ItemAppInfo appInfo, int favouritePosition) {
        if (appInfo != null) {
            if (appInfo.getPackageName() == null) {
                Intent intentAllApp = new Intent(this, AllAppActivity.class);
                intentAllApp.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intentAllApp.putExtra(SPUtils.INTENT_ALL_APP, false);
                intentAllApp.putExtra(SPUtils.FAVOURITE_POSITION, favouritePosition);
                startActivity(intentAllApp);
            } else {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
            hideDialog(false);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showVolumeOption() {
        if (volumeOptionBinding == null) {
            volumeOptionBinding = PopupVolumeOptionBinding.inflate(LayoutInflater.from(this));
        }
        if (volumeOptionBinding.getRoot().getParent() == null) {  // Check if it's already added
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
            volumeOptionBinding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int maxAlarmVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            int maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int maxCallVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            int maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            int maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
            volumeOptionBinding.sbAlarm.setMax(maxAlarmVolume);
            volumeOptionBinding.sbMedia.setMax(maxMediaVolume);
            volumeOptionBinding.sbNotification.setMax(maxNotificationVolume);
            volumeOptionBinding.sbRing.setMax(maxRingVolume);
            volumeOptionBinding.sbVoiceCall.setMax(maxCallVolume);
            int alarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            int mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int callVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            int notificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            volumeOptionBinding.sbAlarm.setProgress(alarmVolume);
            volumeOptionBinding.sbMedia.setProgress(mediaVolume);
            volumeOptionBinding.sbNotification.setProgress(notificationVolume);
            volumeOptionBinding.sbRing.setProgress(ringVolume);
            volumeOptionBinding.sbVoiceCall.setProgress(callVolume);
            volumeOptionBinding.ivBack.setOnClickListener(v -> {
                hideDialog(true);
            });

            volumeOptionBinding.sbAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            volumeOptionBinding.sbVoiceCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            volumeOptionBinding.sbRing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            volumeOptionBinding.sbNotification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            volumeOptionBinding.sbMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }


            });
            overlayViewDialog = new View(this);
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
            overlayViewDialog.setLayoutParams(overlayParams);

            overlayViewDialog.setOnTouchListener((v, event) -> {
                hideDialog(false);
                return true;
            });

            try {
                windowManager.addView(overlayViewDialog, overlayParams);
                windowManager.addView(volumeOptionBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideDialog(false);
        }
    }

    private void showAllApp() {
        Intent intentAllApp = new Intent(this, AllAppActivity.class);
        intentAllApp.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentAllApp);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showDialogTimeOut() {
        if (timeOutBinding == null) {
            timeOutBinding = PopupTimeOutBinding.inflate(LayoutInflater.from(this));
        }
        if (timeOutBinding.getRoot().getParent() == null) {  // Check if it's already added
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
            timeOutBinding.backgroundMenu.setBgColorLight(SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
            try {
                ContentResolver contentResolver = getContentResolver();
                int timeoutMillis = Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT);
                changeStateTimeOut(timeoutMillis / 1000, false);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            timeOutBinding.ll15s.setOnClickListener(v -> {
                changeStateTimeOut(15, true);
            });
            timeOutBinding.ll1m.setOnClickListener(v -> {
                changeStateTimeOut(60, true);
            });
            timeOutBinding.ll10m.setOnClickListener(v -> {
                changeStateTimeOut(600, true);
            });
            timeOutBinding.ll15m.setOnClickListener(v -> {
                changeStateTimeOut(900, true);
            });
            timeOutBinding.ll30m.setOnClickListener(v -> {
                changeStateTimeOut(1800, true);
            });
            timeOutBinding.ll5m.setOnClickListener(v -> {
                changeStateTimeOut(300, true);
            });
            timeOutBinding.ivBack.setOnClickListener(v -> {
                hideDialog(true);
            });
            overlayViewDialog = new View(this);
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
            overlayViewDialog.setLayoutParams(overlayParams);

            overlayViewDialog.setOnTouchListener((v, event) -> {
                hideDialog(false);
                return true;
            });

            try {
                windowManager.addView(overlayViewDialog, overlayParams);
                windowManager.addView(timeOutBinding.getRoot(), popupParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hideDialog(false);
        }
    }

    private void changeStateTimeOut(int time, boolean isChange) {
        if (timeOutBinding != null && timeOutBinding.getRoot().getParent() != null) {
            timeOutBinding.iv15s.setImageResource(R.drawable.time_out_button_sn);
            timeOutBinding.iv1m.setImageResource(R.drawable.time_out_button_sn);
            timeOutBinding.iv5m.setImageResource(R.drawable.time_out_button_sn);
            timeOutBinding.iv10m.setImageResource(R.drawable.time_out_button_sn);
            timeOutBinding.iv15m.setImageResource(R.drawable.time_out_button_sn);
            timeOutBinding.iv30m.setImageResource(R.drawable.time_out_button_sn);
            switch (time) {
                case 15:
                    timeOutBinding.iv15s.setImageResource(R.drawable.time_out_button_s);
                    break;
                case 60:
                    timeOutBinding.iv1m.setImageResource(R.drawable.time_out_button_s);
                    break;
                case 300:
                    timeOutBinding.iv5m.setImageResource(R.drawable.time_out_button_s);
                    break;
                case 600:
                    timeOutBinding.iv10m.setImageResource(R.drawable.time_out_button_s);
                    break;
                case 900:
                    timeOutBinding.iv15m.setImageResource(R.drawable.time_out_button_s);
                    break;
                case 1800:
                    timeOutBinding.iv30m.setImageResource(R.drawable.time_out_button_s);
                    break;

            }
            if (isChange) changeTimeOut(time * 1000);
        }
    }

    private void changeTimeOut(int time) {
        try {
            ContentResolver contentResolver = getContentResolver();
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, time);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("timeout_check", "error: ", e);
        }
    }

    private void hideDialog(boolean isBack) {
        try {
            if (timeOutBinding != null && timeOutBinding.getRoot().getParent() != null) {
                windowManager.removeView(timeOutBinding.getRoot());
            }
            if (volumeOptionBinding != null && volumeOptionBinding.getRoot().getParent() != null) {
                windowManager.removeView(volumeOptionBinding.getRoot());
            }
            if (favouriteBinding != null && favouriteBinding.getRoot().getParent() != null) {
                windowManager.removeView(favouriteBinding.getRoot());
            }
            if (brightnessBinding != null && brightnessBinding.getRoot().getParent() != null) {
                windowManager.removeView(brightnessBinding.getRoot());
            }
            if (overlayViewDialog != null && overlayViewDialog.getParent() != null) {
                windowManager.removeView(overlayViewDialog);
            }
            overlayViewDialog = null;
            if (isBack) {
                if (isShowMenu1) showPopupChoose();
                else showPopupDevice();
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public void takeScreenshot(MediaProjection mediaProjection) {
        ImageReader imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
        VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenCapture", screenWidth, screenHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);

        imageReader.setOnImageAvailableListener(reader -> {
            Image image = reader.acquireLatestImage();
            if (image != null) {
                // Lưu ảnh
                saveImage(image);
                image.close();
                mediaProjection.stop();
            }
        }, null);
    }

    private void saveImage(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);

        // Lưu vào file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ImageUtils.saveImageToMediaStore(this, bitmap);
        } else {
            ImageUtils.saveBitmap(this, bitmap);
        }
    }

    @SuppressLint({"RestrictedApi", "ObjectAnimatorBinding"})
    private void smoothMoveView(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams params,
            int targetX,
            int targetY
    ) {
        ValueAnimator animatorX = ValueAnimator.ofInt(params.x, targetX);
        animatorX.setDuration(300);
        animatorX.setInterpolator(new DecelerateInterpolator());
        animatorX.addUpdateListener(animation -> {
            params.x = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, params);
        });

        ValueAnimator animatorY = ValueAnimator.ofInt(params.y, targetY);
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

    @SuppressLint({"RestrictedApi", "ObjectAnimatorBinding"})
    private void smoothMoveViewVolume(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams paramsVolume,
            int targetX,
            int targetY
    ) {
        ValueAnimator animatorX = ValueAnimator.ofInt(paramsVolume.x, targetX);
        animatorX.setDuration(300);
        animatorX.setInterpolator(new DecelerateInterpolator());
        animatorX.addUpdateListener(animation -> {
            paramsVolume.x = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, paramsVolume);
        });

        ValueAnimator animatorY = ValueAnimator.ofInt(paramsVolume.y, targetY);
        animatorY.setDuration(300);
        animatorY.setInterpolator(new DecelerateInterpolator());
        animatorY.addUpdateListener(animation -> {
            paramsVolume.y = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, paramsVolume);
        });

        animatorX.start();
        animatorY.start();
    }

    public void updatePositionAfterMoveVolume() {
        updatePositionAfterMoveVolume(volumeView, windowManager, paramsVolume);
    }

    public void updatePositionAfterMoveVolume(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams paramsVolume
    ) {
        int centerX = paramsVolume.x + view.getWidth();
        int centerY = paramsVolume.y + view.getHeight();
        int targetX;
        int targetY;
        if (centerX > screenWidth / 2 && centerY > screenHeight / 2) { // Bottom-right quadrant
            if (screenHeight - centerY <= screenWidth - centerX) {
                targetX = Math.min(paramsVolume.x, screenWidth);
                targetY = screenHeight - view.getHeight() - view.getHeight() / 2;
            } else {
                targetX = screenWidth - view.getWidth() + view.getWidth() * SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) / 100;
                targetY = Math.min(paramsVolume.y, screenHeight);
            }
        } else if (centerX > screenWidth / 2 && centerY <= screenHeight / 2) { //top-right
            if (screenWidth - centerX <= centerY) {
                targetX = screenWidth - view.getWidth() + view.getWidth() * SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) / 100;
                targetY = Math.max(paramsVolume.y, 0);
            } else {
                targetX = Math.min(paramsVolume.x, screenWidth);
                targetY = 0;
            }
        } else if (centerX <= screenWidth / 2 && centerY > screenHeight / 2) { //bottom-lèt
            if (screenHeight - centerY <= centerX) {
                targetX = Math.max(paramsVolume.x, 0);
                targetY = screenHeight - view.getHeight() - view.getHeight() / 2;
            } else {
                targetX = -view.getWidth() * SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) / 100;
                targetY = Math.min(paramsVolume.y, screenHeight);
            }
        } else { //top-le
            if (centerX <= centerY) {
                targetX = -view.getWidth() * SPUtils.getInt(this, SPUtils.VOLUME_BUTTON_DISTANCE, 0) / 100;
                targetY = Math.max(paramsVolume.y, 0);
            } else {
                targetX = Math.max(paramsVolume.x, 0);
                targetY = 0;
            }
        }
        smoothMoveViewVolume(view, windowManager, paramsVolume, targetX, targetY);
    }

}
