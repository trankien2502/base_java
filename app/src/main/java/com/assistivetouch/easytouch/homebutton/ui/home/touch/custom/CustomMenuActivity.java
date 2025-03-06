package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.widget.ViewPager2;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityCustomMenuBinding;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorPickerDialog;
import com.assistivetouch.easytouch.homebutton.dialog.pick_color.ColorSelectCallBack;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

public class CustomMenuActivity extends BaseActivity<ActivityCustomMenuBinding> {

    MenuAdapter menuAdapter;
    int oldColor;
    int currentColor;

    @Override
    public ActivityCustomMenuBinding getBinding() {
        return ActivityCustomMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        oldColor = SPUtils.getInt(this, SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default);
        currentColor = oldColor;
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
            if (binding.viewPager.getCurrentItem() == 0) {
                Menu1Fragment.instance.restore();
                Log.e("menu_check", "click1");
            } else {
                Menu2Fragment.instance.restore();
                Log.e("menu_check", "click2");
            }

        });
        binding.ivGone.setOnClickListener(v -> {
            SPUtils.setList(this, SPUtils.MENU_FUNCTION_1, Menu1Fragment.instance.listCurrent);
            SPUtils.setList(this, SPUtils.MENU_FUNCTION_2, Menu2Fragment.instance.listCurrent);
            SPUtils.setInt(this, SPUtils.MENU_BACKGROUND_COLOR, currentColor);
            onBack();
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
        ColorPickerDialog dialog = new ColorPickerDialog(this, true);
        dialog.init(new ColorSelectCallBack() {
            @Override
            public void select(int color) {
                currentColor = color;
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