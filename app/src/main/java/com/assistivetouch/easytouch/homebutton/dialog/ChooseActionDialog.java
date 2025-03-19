package com.assistivetouch.easytouch.homebutton.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseBottomSheetDialog;
import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogChooseActionBinding;
import com.assistivetouch.easytouch.homebutton.databinding.PopupVoulumeConfig3Binding;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;


public class ChooseActionDialog extends BaseBottomSheetDialog<DialogChooseActionBinding> {

    public ChooseActionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogChooseActionBinding setBinding() {
        return DialogChooseActionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void bindView() {
    }



    @Override
    protected void onStop() {
        super.onStop();
    }
}
