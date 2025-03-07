package com.assistivetouch.easytouch.homebutton.ui.screenshot;

import android.content.ContentValues;
import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.io.File;
import java.io.FileDescriptor;


public class RecorderManager {
    private final Context c;
    private final ScreenshotResult screenshotResult;
    private String filePath;
    private boolean isRec;
    private int mHeight;
    private MediaRecorder mMediaRecorder;
    private Uri mUri;
    private VirtualDisplay mVirtualDisplay;
    private int mWidth;
    private MediaProjection mediaProjection;

    public RecorderManager(Context context, ScreenshotResult screenshotResult) {
        this.c = context;
        this.screenshotResult = screenshotResult;
    }

    public void startRecording(MediaProjection mediaProjection, boolean z) {
        SPUtils.vibration(this.c);
        this.mediaProjection = mediaProjection;
        int[] sizes = SPUtils.getSizes(this.c);
        if (z) {
            this.mWidth = sizes[0];
            this.mHeight = sizes[1];
        } else {
            this.mWidth = sizes[1];
            this.mHeight = sizes[0];
        }
        this.mMediaRecorder = new MediaRecorder();
        prepareRecording();
        this.mVirtualDisplay = getVirtualDisplay();
        this.mMediaRecorder.start();
        this.isRec = true;
    }

    public void stopRecording() {
        if (this.isRec) {
            SPUtils.vibration(this.c);
            this.isRec = false;
            MediaRecorder mediaRecorder = this.mMediaRecorder;
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                this.mMediaRecorder.reset();
                this.mMediaRecorder = null;
            }
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            MediaProjection mediaProjection = this.mediaProjection;
            if (mediaProjection != null) {
                mediaProjection.stop();
                this.mediaProjection = null;
            }
            if (Build.VERSION.SDK_INT >= 29) {
                updateGalleryUri();
            } else {
                refreshGalleryFile();
            }
        }
    }

    private VirtualDisplay getVirtualDisplay() {
        return this.mediaProjection.createVirtualDisplay(getClass().getSimpleName(), this.mWidth, this.mHeight, this.c.getResources().getDisplayMetrics().densityDpi, 16, this.mMediaRecorder.getSurface(), null, null);
    }

    private void prepareRecording() {
        CamcorderProfile camcorderProfile;
        if (this.mUri != null) {
            this.mUri = null;
        }
        if (this.filePath != null) {
            this.filePath = null;
        }
        if (Build.VERSION.SDK_INT < 29) {
            makePath();
        } else {
            String str = "video_" + System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("relative_path", Environment.DIRECTORY_MOVIES + File.separator + "RecordScreen");
            contentValues.put("title", str);
            contentValues.put("_display_name", str);
            contentValues.put("mime_type", "video/mp4");
            this.mUri = this.c.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        ItemVideoConfig record = SPUtils.getRecord(this.c);
        if (!record.isAdvance()) {
            try {
                camcorderProfile = CamcorderProfile.get(record.getQuality());
            } catch (RuntimeException unused) {
                camcorderProfile = CamcorderProfile.get(1);
            }
            if (record.isEnaAudio()) {
                this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            }
            this.mMediaRecorder.setVideoSource(2);
            this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            if (record.isEnaAudio()) {
                this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            }
            this.mMediaRecorder.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
            this.mMediaRecorder.setVideoFrameRate(camcorderProfile.videoFrameRate);
        } else {
            if (record.isEnaAudio()) {
                this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                this.mMediaRecorder.setAudioChannels(record.getChannels());
            }
            this.mMediaRecorder.setVideoSource(2);
            this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            this.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            this.mMediaRecorder.setVideoEncodingBitRate(record.getBitrateVideo());
            this.mMediaRecorder.setVideoFrameRate(record.getFrameRate());
            if (record.isEnaAudio()) {
                this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                this.mMediaRecorder.setAudioEncodingBitRate(record.getBitrateAudio());
                this.mMediaRecorder.setAudioSamplingRate(record.getSampleRate());
            }
        }
        this.mMediaRecorder.setVideoSize(this.mWidth, this.mHeight);
        if (this.mUri == null) {
            this.mMediaRecorder.setOutputFile(this.filePath);
        } else {
            try {
                FileDescriptor fileDescriptor = this.c.getContentResolver().openFileDescriptor(this.mUri, "rw").getFileDescriptor();
                if (fileDescriptor != null) {
                    this.mMediaRecorder.setOutputFile(fileDescriptor);
                } else {
                    makePath();
                    this.mMediaRecorder.setOutputFile(this.filePath);
                }
            } catch (Exception unused2) {
                makePath();
                this.mMediaRecorder.setOutputFile(this.filePath);
            }
        }
        try {
            this.mMediaRecorder.prepare();
        } catch (Exception unused3) {
            Toast.makeText(this.c, (int) R.string.error, Toast.LENGTH_SHORT).show();
        }
    }

    private void makePath() {
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "RecordScreen";
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            Toast.makeText(this.c, (int) R.string.error_sd, Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(str);
        if (file.exists() ? true : file.mkdir()) {
            this.filePath = str + File.separator + "video_" + System.currentTimeMillis() + ".mp4";
            return;
        }
        Toast.makeText(this.c, (int) R.string.error_record, Toast.LENGTH_SHORT).show();
    }

    private void refreshGalleryFile() {
        MediaScannerConnection.scanFile(this.c, new String[]{this.filePath}, null, null);
        this.screenshotResult.onImageResult(Uri.parse(this.filePath), true);
    }

    private void updateGalleryUri() {
        Uri uri = this.mUri;
        if (uri != null) {
            this.screenshotResult.onImageResult(uri, true);
            return;
        }
        String str = this.filePath;
        if (str != null) {
            this.screenshotResult.onImageResult(Uri.parse(str), true);
        }
    }
}
