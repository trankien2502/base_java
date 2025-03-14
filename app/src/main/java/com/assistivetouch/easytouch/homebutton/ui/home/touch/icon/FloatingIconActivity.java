package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFloatingIconBinding;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;

public class FloatingIconActivity extends BaseActivity<ActivityFloatingIconBinding> {

    Handler handler = new Handler();
    @Override
    public ActivityFloatingIconBinding getBinding() {
        return ActivityFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ItemFunctionIcon> list = SPUtils.getListFloatingIcon();
        ItemFunctionIcon itemSingleTap = SPUtils.getObject(this,SPUtils.FLOATING_ICON_SINGLE_TAP,list.get(3));
        ItemFunctionIcon itemDoubleTap = SPUtils.getObject(this,SPUtils.FLOATING_ICON_DOUBLE_TAP, list.get(0));
        ItemFunctionIcon itemLongPress = SPUtils.getObject(this,SPUtils.FLOATING_ICON_LONG_PRESS, list.get(0));
        binding.tvSingleTap.setText(itemSingleTap.getText());
        binding.tvDoubleTap.setText(itemDoubleTap.getText());
        binding.tvLongPress.setText(itemLongPress.getText());
    }

    @Override
    public void bindView() {
        binding.clIconStyle.setOnClickListener(v -> {
            resultLauncher.launch(new Intent(this, IconStyleActivity.class));
        });
        binding.clSingleTap.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION,SPUtils.FLOATING_ICON_SINGLE_TAP);
            resultLauncher.launch(intent);
        });
        binding.clDoubleTap.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION,SPUtils.FLOATING_ICON_DOUBLE_TAP);
            resultLauncher.launch(intent);
        });
        binding.clLongPress.setOnClickListener(v -> {
            Intent intent = new Intent(this, FunctionFloatingIconActivity.class);
            intent.putExtra(SPUtils.INTENT_SELECT_FUNCTION,SPUtils.FLOATING_ICON_LONG_PRESS);
            resultLauncher.launch(intent);
        });
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {

        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {

        } else if (result.getResultCode() == 2502) {
            binding.llSuccess.setVisibility(VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.llSuccess.setVisibility(GONE);
                }
            },1500);
        }
    });
}