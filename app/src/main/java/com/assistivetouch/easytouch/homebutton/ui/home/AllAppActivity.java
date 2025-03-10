package com.assistivetouch.easytouch.homebutton.ui.home;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityAllAppBinding;
import com.assistivetouch.easytouch.homebutton.dialog.LoadingDialog;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppCallBack;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfo;
import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfoAdapter;
import com.assistivetouch.easytouch.homebutton.service.ServiceScreen;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class AllAppActivity extends BaseActivity<ActivityAllAppBinding> {

    ItemAppInfoAdapter adapter;
    List<ItemAppInfo> list = new ArrayList<>();
    private PackageManager packageManager;
    LoadingDialog dialog;
    boolean isAllApp = true;
    int favouritePosition;
    ArrayList<ItemAppInfo> favouriteList;

    @Override
    public ActivityAllAppBinding getBinding() {
        return ActivityAllAppBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        favouriteList = SPUtils.getListFavourite(this, SPUtils.FAVOURITE_APP, SPUtils.getListDefaultFavourite(this));
        isAllApp = getIntent().getBooleanExtra(SPUtils.INTENT_ALL_APP, true);
        favouritePosition = getIntent().getIntExtra(SPUtils.FAVOURITE_POSITION, 0);
        showLoadingDialog();
        new Thread(() -> {
            getInstalledApps();
            runOnUiThread(() -> {
                dismissLoadingDialog();
                adapter = new ItemAppInfoAdapter(this, list, new ItemAppCallBack() {
                    @Override
                    public void select(ItemAppInfo appInfo) {
                        if (isAllApp) {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                            if (launchIntent != null) {
                                startActivity(launchIntent);
                            } else {
                                Toast.makeText(getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                            if (launchIntent != null) {
                                appInfo.setId(favouritePosition);
                                appInfo.setIcon(null);
                                favouriteList.set(favouritePosition, appInfo);
                                SPUtils.setListFavourite(getBaseContext(), SPUtils.FAVOURITE_APP, favouriteList);
                                if (ServiceScreen.instance != null)
                                    ServiceScreen.instance.showFavourite();
                                onBack();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                binding.rcvApp.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void getInstalledApps() {
        if (packageManager == null) {
            packageManager = getPackageManager(); // Khởi tạo lại nếu cần
        }
        list.clear();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // Lọc ứng dụng hệ thống
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                String packageName = packageInfo.packageName;
                Drawable icon = packageManager.getApplicationIcon(appInfo);
//                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
//                if (launchIntent != null)
                list.add(new ItemAppInfo(appName, packageName, icon));
            }
        }
    }

    private void showLoadingDialog() {
        dialog = new LoadingDialog(this, false);
        dialog.show();
    }

    private void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}