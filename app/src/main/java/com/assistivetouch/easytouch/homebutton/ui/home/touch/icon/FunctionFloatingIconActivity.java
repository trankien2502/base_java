package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFunctionFloatingIconBinding;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionCallBack;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionIconAdapter;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class FunctionFloatingIconActivity extends BaseActivity<ActivityFunctionFloatingIconBinding> {

    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    ItemFunctionIconAdapter adapter;
    ItemFunctionIcon functionIcon;
    String type = "";
    ArrayList<ItemFunctionIcon> list;

    @Override
    public ActivityFunctionFloatingIconBinding getBinding() {
        return ActivityFunctionFloatingIconBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        list = SPUtils.getListFloatingIcon();
        type = getIntent().getStringExtra(SPUtils.INTENT_SELECT_FUNCTION);
        initData();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, true, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {
                functionIcon = icon;
            }
        });
        binding.rcvFunction.setAdapter(adapter);
        if (type.equals(SPUtils.FLOATING_ICON_SINGLE_TAP))
            adapter.setCheckIcon(SPUtils.getObject(this, type, list.get(3)));
        else
            adapter.setCheckIcon(SPUtils.getObject(this, type, list.get(0)));

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
        functionIconList = SPUtils.getListFloatingIcon();
    }
}