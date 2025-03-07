package com.assistivetouch.easytouch.homebutton.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class SaveUtils {


    public static Uri saveBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, String str, String str2, String str3) throws IOException {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 29) {
            String str4 = Environment.DIRECTORY_DCIM;
            if (!str2.isEmpty()) {
                str4 = str4 + File.separator + str2;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str3);
            contentValues.put("mime_type", str);
            contentValues.put("relative_path", str4);
            ContentResolver contentResolver = context.getContentResolver();
            try {
                try {
                    try {
                        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        try {
                            if (uri == null) {
                                throw new IOException("Failed to create new MediaStore record.");
                            }
                            OutputStream openOutputStream = contentResolver.openOutputStream(uri);
                            try {
                                if (openOutputStream == null) {
                                    throw new IOException("Failed to get output stream.");
                                }
                                if (bitmap.compress(compressFormat, 95, openOutputStream)) {
                                    if (openOutputStream != null) {
                                        openOutputStream.close();
                                    }
                                    return uri;
                                }
                                throw new IOException("Failed to save bitmap.");
                            } catch (IOException e) {
                                e = e;
                                if (uri != null) {
                                    contentResolver.delete(uri, null, null);
                                }
                                throw e;
                            }
                        } catch (IOException e2) {

                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (IOException e3) {

                    uri = null;
                }
            } catch (Throwable th2) {

            }

            return uri;
        } else {
            String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            if (!str2.isEmpty()) {
                file = file + File.separator + str2;
            }
            File file2 = new File(file);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file, str3 + (compressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".jpg"));
            file3.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(compressFormat, 95, fileOutputStream);
            fileOutputStream.close();
            MediaScannerConnection.scanFile(context, new String[]{file3.getPath()}, null, null);
            return Uri.fromFile(file3);
        }
    }


}
