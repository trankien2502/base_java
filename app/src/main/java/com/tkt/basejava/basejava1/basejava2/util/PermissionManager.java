package com.tkt.basejava.basejava1.basejava2.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

public class PermissionManager {

    public boolean checkReadPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
            }
            return false;
        }
    }

    public static boolean checkNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else return true;

    }

    public static boolean checkOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else return true;
    }

    public static boolean checkMicrophonePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else return true;
    }

    public static boolean checkCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkSwNotification();
//            }
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                checkSwNotification();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                        countNotification++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
//                        SPUtils.setInt(this, SPUtils.NOTIFICATION, countNotification);
//                        if (countNotification > 1) {
//                            if (!isShowDialog) {
//                                isShowDialog = true;
//                                showDialogGotoSetting(2);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkSwStorage();
//            }
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                checkSwStorage();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
//                            countStorage++;
//                            AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
//                            SPUtils.setInt(this, SPUtils.STORAGE, countStorage);
//                            if (countStorage > 1) {
//                                if (!isShowDialog) {
//                                    isShowDialog = true;
//                                    showDialogGotoSetting(4);
//                                }
//                            }
//                        }
//                    } else {
//                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                            countStorage++;
//                            AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
//                            SPUtils.setInt(this, SPUtils.STORAGE, countStorage);
//                            if (countStorage > 1) {
//                                if (!isShowDialog) {
//                                    isShowDialog = true;
//                                    showDialogGotoSetting(3);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkSwAudio();
//            }
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                checkSwAudio();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
//                        countAudio++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
//                        SPUtils.setInt(this, SPUtils.AUDIO, countAudio);
//                        if (countAudio > 1) {
//                            if (!isShowDialog) {
//                                isShowDialog = true;
//                                showDialogGotoSetting(1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                checkSwCamera();
//            }
//
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                checkSwCamera();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                        countCamera++;
//                        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
//                        SPUtils.setInt(this, SPUtils.CAMERA, countCamera);
//                        if (countCamera > 1) {
//                            if (!isShowDialog) {
//                                isShowDialog = true;
//                                showDialogGotoSetting(5);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (!isShowDialog) {
//            if (IsNetWork.haveNetworkConnection(PermissionActivity.this) && CommonAdsApi.listIDAdsNativePermission.size() != 0 && RemoteConfig.remote_native_permission && CheckAds.getInstance().isShowAds(this)) {
//                binding.nativePer.setVisibility(View.VISIBLE);
//            } else {
//                binding.nativePer.setVisibility(View.INVISIBLE);
//            }
//        }
//    }

}
