package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.view.View;
import android.widget.Toast;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityFunctionCustomMenuBinding;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionCallBack;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.item.ItemFunctionIconAdapter;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class FunctionCustomMenuActivity extends BaseActivity<ActivityFunctionCustomMenuBinding> {

    List<ItemFunctionIcon> functionIconList = new ArrayList<>();
    List<ItemFunctionIcon> functionSelectedList = new ArrayList<>();

    ItemFunctionIconAdapter adapter, adapterSelect;
    ItemFunctionIcon currentSelectItem = null;
    int menuFunction;
    int menuPosition;

    @Override
    public ActivityFunctionCustomMenuBinding getBinding() {
        return ActivityFunctionCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        menuFunction = getIntent().getIntExtra(SPUtils.MENU_FUNCTION, 1);
        menuPosition = getIntent().getIntExtra(SPUtils.MENU_POSITION, 0);
        if (menuFunction == 1) {
            if (Menu1Fragment.instance.listCurrent != null)
                functionSelectedList.addAll(Menu1Fragment.instance.listCurrent);
        } else {
            if (Menu2Fragment.instance.listCurrent != null)
                functionSelectedList.addAll(Menu2Fragment.instance.listCurrent);
        }
        initData();
        Toast.makeText(this, "menu & position: " + menuFunction + menuPosition, Toast.LENGTH_SHORT).show();
        adapter = new ItemFunctionIconAdapter(this, functionIconList, true,new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {
                adapter.setCheckIcon(icon);
                currentSelectItem = icon;
            }
        });
        adapterSelect = new ItemFunctionIconAdapter(this, functionSelectedList,false, new ItemFunctionCallBack() {
            @Override
            public void select(ItemFunctionIcon icon) {

            }
        });
        binding.rcvFunctionSelect.setAdapter(adapterSelect);
        binding.rcvFunction.setAdapter(adapter);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.ivGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectItem != null) {
                    currentSelectItem.setId(menuPosition);
                    if (menuFunction == 1) {
                        Menu1Fragment.instance.listCurrent.set(menuPosition, currentSelectItem);
                    } else {
                        Menu2Fragment.instance.listCurrent.set(menuPosition, currentSelectItem);
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(FunctionCustomMenuActivity.this, R.string.please_select_a_function_first, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void selectedList(List<ItemFunctionIcon> list) {
        List<ItemFunctionIcon> listDelete = new ArrayList<>();
        for (ItemFunctionIcon icon : list) {
            for (ItemFunctionIcon icon1 : functionIconList)
                if (icon.getText() == icon1.getText())
                    listDelete.add(icon1);
        }
        for (ItemFunctionIcon icon : listDelete) {
            if (icon.getText() != R.string.none)
                functionIconList.remove(icon);
        }
    }

    private void initData() {
        functionIconList = SPUtils.getListCustomMenu();
        selectedList(functionSelectedList);
    }
}