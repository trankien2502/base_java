package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseFragment;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectAction2Binding;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;

public class Menu2Fragment extends BaseFragment<PopupSelectAction2Binding> {

    ArrayList<ItemFunctionIcon> listDefault = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listCurrent = new ArrayList<>();
    public static Menu2Fragment instance;

    @Override
    public PopupSelectAction2Binding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return PopupSelectAction2Binding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        instance = this;
        listDefault = SPUtils.getListDefaultMenu2();
        listCurrent = SPUtils.getList(requireContext(), SPUtils.MENU_FUNCTION_2, listDefault);
        binding.backgroundMenu.setBgColorLight(SPUtils.getInt(requireContext(), SPUtils.MENU_BACKGROUND_COLOR,R.color.color_default));
    }

    @Override
    public void bindView() {
        binding.llAction1.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 0);
            startArc(intent);
        });
        binding.llAction2.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 1);
            startArc(intent);
        });
        binding.llAction3.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 2);
            startArc(intent);
        });
        binding.llAction5.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 3);
            startArc(intent);
        });
        binding.llAction6.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 4);
            startArc(intent);
        });
        binding.llAction7.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION, 2);
            intent.putExtra(SPUtils.MENU_POSITION, 5);
            startArc(intent);
        });
    }

    private void startArc(Intent intent) {
        if (getContext() instanceof CustomMenuActivity) {
            CustomMenuActivity customMenuActivity = (CustomMenuActivity) getContext();
            customMenuActivity.resultLauncher.launch(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listCurrent != null && !listCurrent.isEmpty()) {
            binding.imgAction1.setImageResource(listCurrent.get(0).getIconShow());
            binding.txtAction1.setText(listCurrent.get(0).getText());
            binding.imgAction2.setImageResource(listCurrent.get(1).getIconShow());
            binding.txtAction2.setText(listCurrent.get(1).getText());
            binding.imgAction3.setImageResource(listCurrent.get(2).getIconShow());
            binding.txtAction3.setText(listCurrent.get(2).getText());
            binding.imgAction5.setImageResource(listCurrent.get(3).getIconShow());
            binding.txtAction5.setText(listCurrent.get(3).getText());
            binding.imgAction6.setImageResource(listCurrent.get(4).getIconShow());
            binding.txtAction6.setText(listCurrent.get(4).getText());
            binding.imgAction7.setImageResource(listCurrent.get(5).getIconShow());
            binding.txtAction7.setText(listCurrent.get(5).getText());
        }
    }

    public void restore() {
        listCurrent.clear();
        listCurrent.addAll(listDefault);
        Log.e("menu_check","menu2 restore"+listCurrent);
        if (listCurrent != null && !listCurrent.isEmpty()) {
            Log.e("menu_check","menu2 start restore");
            binding.imgAction1.setImageResource(listCurrent.get(0).getIconShow());
            binding.txtAction1.setText(listCurrent.get(0).getText());
            binding.imgAction2.setImageResource(listCurrent.get(1).getIconShow());
            binding.txtAction2.setText(listCurrent.get(1).getText());
            binding.imgAction3.setImageResource(listCurrent.get(2).getIconShow());
            binding.txtAction3.setText(listCurrent.get(2).getText());
            binding.imgAction5.setImageResource(listCurrent.get(3).getIconShow());
            binding.txtAction5.setText(listCurrent.get(3).getText());
            binding.imgAction6.setImageResource(listCurrent.get(4).getIconShow());
            binding.txtAction6.setText(listCurrent.get(4).getText());
            binding.imgAction7.setImageResource(listCurrent.get(5).getIconShow());
            binding.txtAction7.setText(listCurrent.get(5).getText());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
    
}