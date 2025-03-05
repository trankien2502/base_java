package com.tkt.basejava.basejava1.basejava2.ui.home.touch.custom;

import android.content.Intent;
import android.graphics.Color;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.widget.ViewPager2;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityCustomMenuBinding;
import com.tkt.basejava.basejava1.basejava2.dialog.pick_color.ColorPickerDialog;
import com.tkt.basejava.basejava1.basejava2.dialog.pick_color.ColorSelectCallBack;

public class CustomMenuActivity extends BaseActivity<ActivityCustomMenuBinding> {

    MenuAdapter menuAdapter;

    @Override
    public ActivityCustomMenuBinding getBinding() {
        return ActivityCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        menuAdapter = new MenuAdapter(this);
        binding.viewPager.setAdapter(menuAdapter);
        binding.viewPager.setCurrentItem(1);
        binding.viewPager.setCurrentItem(0);
    }

    @Override
    public void bindView() {
        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });
        binding.menu1.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(0);
            changeState(0);
        });
        binding.menu2.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
            changeState(1);
        });
        binding.llColor.setOnClickListener(v -> {
            showColorPickerDialog();
        });
        binding.llRestore.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem()==0){
                Menu1Fragment.instance.restore();
            } else {
                Menu1Fragment.instance.restore();
            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeState(position);
            }
        });
    }

    private void showColorPickerDialog() {
        ColorPickerDialog dialog = new ColorPickerDialog(this,true);
        dialog.init(new ColorSelectCallBack() {
            @Override
            public void select(int color) {
                Menu1Fragment.instance.binding.backgroundMenu.setBgColorLight(color);
                Menu2Fragment.instance.binding.backgroundMenu.setBgColorLight(color);
            }
        });
        dialog.show();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {

        }
    });

    private void changeState(int position) {
        if (position == 0) {
            binding.menu1.setBackgroundResource(R.drawable.bg_menu_s);
            binding.menu1.setTextColor(Color.WHITE);
            binding.menu2.setBackgroundResource(R.drawable.bg_menu_sn);
            binding.menu2.setTextColor(Color.parseColor("#8a8a8a"));
        } else {
            binding.menu2.setBackgroundResource(R.drawable.bg_menu_s);
            binding.menu2.setTextColor(Color.WHITE);
            binding.menu1.setBackgroundResource(R.drawable.bg_menu_sn);
            binding.menu1.setTextColor(Color.parseColor("#8a8a8a"));
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

}