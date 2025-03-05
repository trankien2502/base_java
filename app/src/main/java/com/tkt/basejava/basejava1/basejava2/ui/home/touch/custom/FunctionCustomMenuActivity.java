package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityFunctionCustomMenuBinding;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionCallBack;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIcon;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIconAdapter;

import java.util.ArrayList;
import java.util.List;

public class FunctionCustomMenuActivity extends BaseActivity<ActivityFunctionCustomMenuBinding> {

    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    ItemFunctionIconAdapter adapter;

    @Override
    public ActivityFunctionCustomMenuBinding getBinding() {
        return ActivityFunctionCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {

            }
        });
        binding.rcvFunction.setAdapter(adapter);
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

    private void initData() {
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_none, R.string.none));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_back, R.string.back));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_screen_recorder, R.string.screen_recorder));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_bluetooth, R.string.bluetooth));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_airplane, R.string.airplane));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_location, R.string.location));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_lock_rotation, R.string.lock_rotation));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_flashlight, R.string.flashlight));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_volume_cross, R.string.volume_option));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_time_out, R.string.time_out));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_all_app, R.string.all_app));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_home, R.string.home));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_wifi, R.string.wifi));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_bright_ness, R.string.brightness));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_device, R.string.device));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_screen_shoot, R.string.screen_shoot));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_notification, R.string.notification));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_favourite, R.string.favourite));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_recent, R.string.recent));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_lock_screen, R.string.lock_screen));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_power, R.string.power));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_setting, R.string.settings));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_volume_cross_up, R.string.volume_up));
        functionIconList.add(new ItemFunctionIcon(R.drawable.ic_function_volume_cross_down, R.string.volume_down));
    }
}