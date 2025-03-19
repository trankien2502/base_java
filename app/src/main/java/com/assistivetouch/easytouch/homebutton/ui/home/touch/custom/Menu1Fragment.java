package com.assistivetouch.easytouch.homebutton.ui.home.touch.custom;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.ads.ConstantIdAds;
import com.assistivetouch.easytouch.homebutton.ads.ConstantRemote;
import com.assistivetouch.easytouch.homebutton.ads.IsNetWork;
import com.assistivetouch.easytouch.homebutton.base.BaseFragment;
import com.assistivetouch.easytouch.homebutton.databinding.PopupSelectActionBinding;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;
import com.assistivetouch.easytouch.homebutton.util.EventTracking;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;

import java.util.ArrayList;

public class Menu1Fragment extends BaseFragment<PopupSelectActionBinding> {

    public static Menu1Fragment instance;
    public ArrayList<ItemFunctionIcon> listDefault = new ArrayList<>();
    public ArrayList<ItemFunctionIcon> listCurrent = new ArrayList<>();

    @Override
    public PopupSelectActionBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return PopupSelectActionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        instance = this;
        loadInterCustom();
        listDefault = SPUtils.getListDefaultMenu1();
        listCurrent = SPUtils.getList(requireContext(), SPUtils.MENU_FUNCTION_1, listDefault);
        binding.backgroundMenu.setBgColorLight(SPUtils.getInt(requireContext(), SPUtils.MENU_BACKGROUND_COLOR, R.color.color_default));
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

    private boolean checkEqual() {
        listDefault = SPUtils.getListDefaultMenu1();
        if (listCurrent.size() != listDefault.size()) return false;
        for (int i = 0; i < listCurrent.size(); i++) {
            if (!listCurrent.get(i).equals(listDefault.get(i))) return false;
        }
        return true;
    }

    public void restore() {
        if (checkEqual()) {
            Toast.makeText(requireContext(), R.string.menu_1_already_reset, Toast.LENGTH_SHORT).show();
        } else {
            listCurrent.clear();
            listCurrent.addAll(SPUtils.getListDefaultMenu1());
            Log.e("menu_check", "menu1 restore" + listCurrent);
            if (!listCurrent.isEmpty()) {
                Log.e("menu_check", "menu1 start restore");
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
                Toast.makeText(requireContext(), R.string.menu_1_reset_successfully, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void bindView() {
        binding.llAction1.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 0);
            showInterCustom(intent);
        });
        binding.llAction2.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 1);
            showInterCustom(intent);
        });
        binding.llAction3.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 2);
            showInterCustom(intent);
        });
        binding.llAction5.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 3);
            showInterCustom(intent);
        });
        binding.llAction6.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 4);
            showInterCustom(intent);
        });
        binding.llAction7.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FunctionCustomMenuActivity.class);
            EventTracking.logEvent(requireContext(), "custom_menu_item_click");
            intent.putExtra(SPUtils.MENU_FUNCTION, 1);
            intent.putExtra(SPUtils.MENU_POSITION, 5);
            showInterCustom(intent);
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
    private void loadInterCustom() {
        if (ConstantIdAds.mInterMenu == null && IsNetWork.haveNetworkConnectionUMP(requireContext()) && !ConstantIdAds.listIDAdsInterMenu.isEmpty() && ConstantRemote.inter_menu  && ConstantRemote.show_ads) {
            ConstantIdAds.mInterMenu = CommonAd.getInstance().getInterstitialAds(requireContext(), ConstantIdAds.listIDAdsInterMenu);
        }
    }
    private void showInterCustom(Intent intent) {
        if (IsNetWork.haveNetworkConnectionUMP(requireContext()) && !ConstantIdAds.listIDAdsInterMenu.isEmpty() && ConstantRemote.inter_menu  && ConstantRemote.show_ads) {
            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start_old > ConstantRemote.interval_interstitial_from_start * 1000) {
                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
                    try {
                        if (ConstantIdAds.mInterMenu != null) {
                            CommonAd.getInstance().forceShowInterstitialByTime(requireContext(), ConstantIdAds.mInterMenu, new CommonAdCallback() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    startArc(intent);
                                }

                                @Override
                                public void onAdClosedByTime() {
                                    super.onAdClosedByTime();
                                    ConstantIdAds.mInterMenu = null;
                                    ConstantRemote.time_interval_old = System.currentTimeMillis();
                                    loadInterCustom();
                                }
                            }, true);
                        } else {
                            loadInterCustom();
                        }
                    } catch (Exception e) {
                        startArc(intent);
                    }
                } else {
                    startArc(intent);
                }
            } else {
                startArc(intent);
            }
        } else {
            startArc(intent);
        }
    }

}