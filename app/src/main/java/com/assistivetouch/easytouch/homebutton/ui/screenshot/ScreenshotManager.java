package com.assistivetouch.easytouch.homebutton.ui.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SaveUtils;

import java.nio.ByteBuffer;


public class ScreenshotManager {
    private final Context c;
    private final ImageAvailableListener listener = new ImageAvailableListener();
    private final Handler mHandler;
    private String NAME;
    private int mHeight;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private int mWidth;
    private MediaProjection mediaProjection;
    private boolean portrait;

    public ScreenshotManager(final Context context, final ScreenshotResult screenshotResult) {
        this.c = context;
        this.mHandler = new Handler(new Handler.Callback() {
            @Override
            public final boolean handleMessage(Message message) {
                return ScreenshotManager.lambda$new$0(context, screenshotResult, message);
            }
        });
    }

    public static boolean lambda$new$0(Context context, ScreenshotResult screenshotResult, Message message) {
        if (message.what == 1) {
            screenshotResult.onImageResult((Uri) message.obj, false);
        }
        return true;
    }

    public void startProjection(final MediaProjection mediaProjection, boolean z) {
        this.portrait = z;
        this.mediaProjection = mediaProjection;
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public final void run() {
                ScreenshotManager.this.m94xe9818b53(mediaProjection);
            }
        }, 700L);
    }


    public void m94xe9818b53(MediaProjection mediaProjection) {
        this.NAME = "screen_" + System.currentTimeMillis();
        createVirtualDisplay();
        mediaProjection.registerCallback(new MediaProjectionStopCallback(), this.mHandler);
    }

    private void createVirtualDisplay() {
        int[] sizes = SPUtils.getSizes(this.c);
        if (this.portrait) {
            this.mWidth = sizes[0];
            this.mHeight = sizes[1];
        } else {
            this.mWidth = sizes[1];
            this.mHeight = sizes[0];
        }
        DisplayMetrics displayMetrics = this.c.getResources().getDisplayMetrics();
        this.mImageReader = ImageReader.newInstance(this.mWidth, this.mHeight, PixelFormat.RGBA_8888, 2);
        this.mVirtualDisplay = this.mediaProjection.createVirtualDisplay("SC_iControl", this.mWidth, this.mHeight, displayMetrics.densityDpi, 9, this.mImageReader.getSurface(), null, this.mHandler);
        this.mImageReader.setOnImageAvailableListener(this.listener, this.mHandler);
    }

    public void done(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public final void run() {
                Log.e("bitmap11", "" + bitmap);
                ScreenshotManager.this.m93xb361615f(bitmap);
            }
        }).start();
    }

    public void m93xb361615f(Bitmap bitmap) {
        Uri saveBitmap;
        try {
            Bitmap cropBitmapTransparency = SPUtils.cropBitmapTransparency(bitmap);

            Log.e("bitmap122", "" + cropBitmapTransparency);

            if (cropBitmapTransparency != null) {
                saveBitmap = SaveUtils.saveBitmap(this.c.getApplicationContext(), cropBitmapTransparency, Bitmap.CompressFormat.JPEG, "image/jpeg", "Screenshots", this.NAME);
            } else {
                saveBitmap = SaveUtils.saveBitmap(this.c.getApplicationContext(), bitmap, Bitmap.CompressFormat.JPEG, "image/jpeg", "Screenshots", this.NAME);
            }
            Message message = new Message();
            message.what = 1;
            message.obj = saveBitmap;
            this.mHandler.sendMessage(message);
        } catch (Exception unused) {
        }
    }

    public void stopProjection() {
        this.mImageReader.close();
        this.mHandler.post(new Runnable() {
            @Override
            public final void run() {
                ScreenshotManager.this.m95x9eb369cf();
            }
        });
    }

    public void m95x9eb369cf() {
        MediaProjection mediaProjection = this.mediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
    }

    public class MediaProjectionStopCallback extends MediaProjection.Callback {
        private MediaProjectionStopCallback() {

        }

        @Override
        public void onStop() {
            ScreenshotManager.this.mHandler.post(new Runnable() {
                @Override
                public final void run() {
                    MediaProjectionStopCallback.this.m96x2677e8f2();
                }
            });
        }


        public void m96x2677e8f2() {
            if (ScreenshotManager.this.mVirtualDisplay != null) {
                ScreenshotManager.this.mVirtualDisplay.release();
            }
            if (ScreenshotManager.this.mImageReader != null) {
                ScreenshotManager.this.mImageReader.setOnImageAvailableListener(null, null);
            }
            ScreenshotManager.this.mediaProjection.unregisterCallback(this);
        }
    }

    public class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        private ImageAvailableListener() {

        }

        @Override
        public void onImageAvailable(ImageReader imageReader) {
            try {
                Image acquireLatestImage = imageReader.acquireLatestImage();
                if (acquireLatestImage != null) {
                    Image.Plane[] planes = acquireLatestImage.getPlanes();
                    if (planes[0].getBuffer() == null) {
                        return;
                    }
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    Bitmap createBitmap = Bitmap.createBitmap(ScreenshotManager.this.mWidth + ((planes[0].getRowStride() - (ScreenshotManager.this.mWidth * pixelStride)) / pixelStride), ScreenshotManager.this.mHeight, Bitmap.Config.ARGB_8888);
                    createBitmap.copyPixelsFromBuffer(buffer);
                    ScreenshotManager.this.done(Bitmap.createBitmap(createBitmap));
                    acquireLatestImage.close();
                    createBitmap.recycle();
                }
            } catch (Exception unused) {
                Toast.makeText(ScreenshotManager.this.c, (int) R.string.error, Toast.LENGTH_SHORT).show();
            }
            ScreenshotManager.this.stopProjection();
        }
    }
}
