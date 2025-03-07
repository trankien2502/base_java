package com.assistivetouch.easytouch.homebutton.util;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.Toast;

import com.assistivetouch.easytouch.homebutton.R;


public class FlashlightProvider {
    private final Context context;
    private final FlashChangeResult flashChangeResult;
    private CameraManager camManager;
    private boolean isOn;
    private Camera mCamera;
    private Camera.Parameters parameters;


    public FlashlightProvider(Context context, FlashChangeResult flashChangeResult) {
        this.context = context;
        this.flashChangeResult = flashChangeResult;
    }

    public void turnFlashlightOn() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                CameraManager cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);
                this.camManager = cameraManager;
                if (cameraManager != null) {
                    this.camManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
                    this.isOn = true;
                } else {
                    this.isOn = false;
                }
            } catch (Exception unused) {
                this.isOn = false;
                Toast.makeText(this.context, (int) R.string.error, Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                Camera open = Camera.open();
                this.mCamera = open;
                Camera.Parameters parameters = open.getParameters();
                this.parameters = parameters;
                parameters.setFlashMode("torch");
                this.mCamera.setParameters(this.parameters);
                this.mCamera.startPreview();
                this.isOn = true;
            } catch (Exception unused2) {
                Toast.makeText(this.context, (int) R.string.error, Toast.LENGTH_SHORT).show();
            }
        }
        this.flashChangeResult.onChangeFlash(this.isOn);
    }

    public void turnFlashlightOff() {
        this.isOn = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                CameraManager cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);
                this.camManager = cameraManager;
                if (cameraManager != null) {
                    this.camManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
                }
            } catch (Exception unused) {
                Toast.makeText(this.context, (int) R.string.error, Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                Camera open = Camera.open();
                this.mCamera = open;
                Camera.Parameters parameters = open.getParameters();
                this.parameters = parameters;
                parameters.setFlashMode("off");
                this.mCamera.setParameters(this.parameters);
                this.mCamera.stopPreview();
            } catch (Exception unused2) {
                Toast.makeText(this.context, (int) R.string.error, Toast.LENGTH_SHORT).show();
            }
        }
        this.flashChangeResult.onChangeFlash(this.isOn);
    }

    public boolean isOn() {
        return this.isOn;
    }

    public interface FlashChangeResult {
        void onChangeFlash(boolean z);
    }
}
