package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.base.BaseFragment;
import com.tkt.basejava.basejava1.basejava2.databinding.PopupSelectActionBinding;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIcon;
import com.tkt.basejava.basejava1.basejava2.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class Menu1Fragment extends BaseFragment<PopupSelectActionBinding> {

    public static Menu1Fragment instance;
    ArrayList<ItemFunctionIcon> listDefault = new ArrayList<>();
    ArrayList<ItemFunctionIcon> listCurrent = new ArrayList<>();

    @Override
    public PopupSelectActionBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return PopupSelectActionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        instance = this;
        initData();
        listCurrent = SPUtils.getList(requireContext(), SPUtils.MENU_FUNCTION_1, listDefault);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listCurrent != null && !listCurrent.isEmpty()) {
            binding.imgAction1.setImageResource(listCurrent.get(0).getIcon());
            binding.txtAction1.setText(listCurrent.get(0).getText());
            binding.imgAction2.setImageResource(listCurrent.get(1).getIcon());
            binding.txtAction2.setText(listCurrent.get(1).getText());
            binding.imgAction3.setImageResource(listCurrent.get(2).getIcon());
            binding.txtAction3.setText(listCurrent.get(2).getText());
            binding.imgAction5.setImageResource(listCurrent.get(3).getIcon());
            binding.txtAction5.setText(listCurrent.get(3).getText());
            binding.imgAction6.setImageResource(listCurrent.get(4).getIcon());
            binding.txtAction6.setText(listCurrent.get(4).getText());
            binding.imgAction7.setImageResource(listCurrent.get(5).getIcon());
            binding.txtAction7.setText(listCurrent.get(5).getText());
        }
    }
    public void restore(){
        listCurrent = listDefault;
        if (listCurrent != null && !listCurrent.isEmpty()) {
            binding.imgAction1.setImageResource(listCurrent.get(0).getIcon());
            binding.txtAction1.setText(listCurrent.get(0).getText());
            binding.imgAction2.setImageResource(listCurrent.get(1).getIcon());
            binding.txtAction2.setText(listCurrent.get(1).getText());
            binding.imgAction3.setImageResource(listCurrent.get(2).getIcon());
            binding.txtAction3.setText(listCurrent.get(2).getText());
            binding.imgAction5.setImageResource(listCurrent.get(3).getIcon());
            binding.txtAction5.setText(listCurrent.get(3).getText());
            binding.imgAction6.setImageResource(listCurrent.get(4).getIcon());
            binding.txtAction6.setText(listCurrent.get(4).getText());
            binding.imgAction7.setImageResource(listCurrent.get(5).getIcon());
            binding.txtAction7.setText(listCurrent.get(5).getText());
        }
    }
    @Override
    public void bindView() {
        binding.llAction1.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,0);
            startArc(intent);
        });
        binding.llAction2.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,1);
            startArc(intent);
        });
        binding.llAction3.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,2);
            startArc(intent);
        });
        binding.llAction5.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,3);
            startArc(intent);
        });
        binding.llAction6.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,4);
            startArc(intent);
        });
        binding.llAction7.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            intent.putExtra(SPUtils.MENU_FUNCTION_1,true);
            intent.putExtra(SPUtils.MENU_POSITION,5);
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
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    private void initData() {
        listDefault.add(new ItemFunctionIcon(0, R.drawable.ic_action_favourite, R.string.favourite));
        listDefault.add(new ItemFunctionIcon(1, R.drawable.ic_action_recent, R.string.recent));
        listDefault.add(new ItemFunctionIcon(2, R.drawable.ic_action_notification, R.string.notification));
        listDefault.add(new ItemFunctionIcon(3, R.drawable.ic_action_home, R.string.home));
        listDefault.add(new ItemFunctionIcon(4, R.drawable.ic_action_device, R.string.device));
        listDefault.add(new ItemFunctionIcon(5, R.drawable.ic_action_airplane, R.string.airplane));
    }
}