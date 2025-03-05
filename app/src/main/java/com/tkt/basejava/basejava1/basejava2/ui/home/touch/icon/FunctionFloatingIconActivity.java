package com.tkt.basejava.basejava1.basejava2.ui.home.touch.icon;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFunctionFloatingIconBinding;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionCallBack;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIcon;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIconAdapter;
import com.tkt.basejava.basejava1.basejava2.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class FunctionFloatingIconActivity extends BaseActivity<ActivityFunctionFloatingIconBinding> {

    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    ItemFunctionIconAdapter adapter;
    ItemFunctionIcon functionIcon;
    String type="";

    @Override
    public ActivityFunctionFloatingIconBinding getBinding() {
        return ActivityFunctionFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        type = getIntent().getStringExtra(SPUtils.INTENT_SELECT_FUNCTION);
        initData();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {
                functionIcon = icon;
            }
        });
        binding.rcvFunction.setAdapter(adapter);
        if (type.equals(SPUtils.FLOATING_ICON_SINGLE_TAP))
            adapter.setCheckIcon(SPUtils.getObject(this, type, new ItemFunctionIcon(R.drawable.ic_function_open_menu, R.string.open_menu)));
        else
            adapter.setCheckIcon(SPUtils.getObject(this, type, new ItemFunctionIcon(R.drawable.ic_function_none, R.string.none)));

    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(v -> {
            if (functionIcon != null) SPUtils.setObject(this, type, functionIcon);
            finish();
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void initData() {
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_none, R.string.none));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_back, R.string.back));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_home, R.string.home));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_open_menu, R.string.open_menu));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_screen_recorder, R.string.screen_recorder));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_flashlight, R.string.flashlight));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_camera, R.string.camera));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_screen_shoot, R.string.screen_shoot));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_notification, R.string.notification));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_recent, R.string.recent));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_lock_screen, R.string.lock_screen));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_power, R.string.power));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_all_app, R.string.all_app));
    }
}