package com.assistivetouch.easytouch.homebutton.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.assistivetouch.easytouch.homebutton.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {
    public static Bitmap loadBitmapImageFromInternalStorage(String filePath) {
        try {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getImagePathFromInternalStorage(Context context, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            if (file.exists()) {
                return file.getAbsolutePath();
            } else {
                Log.e("img_check", "File not found");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveImageToMediaStore(Context context, Bitmap bitmap) {
        ContentResolver resolver = context.getContentResolver();
        Uri imageUri;
        OutputStream fos = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "CatMaker_" + System.currentTimeMillis() + ".png");
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/CatMaker");
            imageUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            try {
                if (imageUri != null) {
                    fos = resolver.openOutputStream(imageUri);
                    assert fos != null;
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    Toast.makeText(context, context.getString(R.string.download_success), Toast.LENGTH_SHORT).show();
                    fos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, context.getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void saveBitmap(Context context, Bitmap bitmap) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Screenshots");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = new File(storageDir, "Screenshot_" + System.currentTimeMillis() + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, context.getString(R.string.image_save_on) + " DCIM/Screenshots", Toast.LENGTH_SHORT).show();
                    }
            );

            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("img_check", "failed down: ", e);
            new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, context.getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
                    }
            );

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
