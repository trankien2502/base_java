package com.assistivetouch.easytouch.homebutton.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.assistivetouch.easytouch.homebutton.MyApplication;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ui.splash.SplashActivity;
import com.assistivetouch.easytouch.homebutton.util.PermissionManager;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class ScreenRecordService extends Service {
    public static ScreenRecordService instance;
    private MediaProjection mediaProjection;
    String filePath;
    private VirtualDisplay virtualDisplay;
    WindowManager windowManager;
    private int screenWidth, screenHeight, screenDensity;
    private MediaRecorder mediaRecorder;
    private MediaProjectionManager projectionManager;
    private Uri mUri;
    public boolean isRecord = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(2, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
        } else {
            startForeground(2, createNotification());
        }
        instance = this;
        Log.e("check_record", "on create");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
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
    }
    private Notification createNotification() {
        SystemUtil.setLocale(this);
        Intent stopIntent = new Intent(this, StopServiceRecordReceiver.class);
        stopIntent.setAction("STOP_SERVICE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(getString(R.string.recording_screen))
                .setContentText(getString(R.string.tap_to_stop_record))
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            Log.e("check_record", "on command start");
            int resultCode = intent.getIntExtra("RESULT_CODE", Activity.RESULT_CANCELED);
            Intent data = intent.getParcelableExtra("DATA_INTENT");

            if (data != null) {
                mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            }
            setupMediaRecorder();
            startRecording();
        }
        return START_STICKY;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        instance = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
