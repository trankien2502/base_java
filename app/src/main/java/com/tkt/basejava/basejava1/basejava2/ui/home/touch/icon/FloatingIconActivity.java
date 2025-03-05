package com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFloatingIconBinding;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIcon;
import com.tkt.basejava.basejava1.basejava2.util.SPUtils;

public class FloatingIconActivity extends BaseActivity<ActivityFloatingIconBinding> {

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
        ItemFunctionIcon itemSingleTap = SPUtils.getObject(this,SPUtils.FLOATING_ICON_SINGLE_TAP, new ItemFunctionIcon(R.drawable.ic_function_open_menu,R.string.open_menu));
        ItemFunctionIcon itemDoubleTap = SPUtils.getObject(this,SPUtils.FLOATING_ICON_DOUBLE_TAP, new ItemFunctionIcon(R.drawable.ic_function_none,R.string.none));
        ItemFunctionIcon itemLongPress = SPUtils.getObject(this,SPUtils.FLOATING_ICON_LONG_PRESS, new ItemFunctionIcon(R.drawable.ic_function_none,R.string.none));
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

        }
    });
}