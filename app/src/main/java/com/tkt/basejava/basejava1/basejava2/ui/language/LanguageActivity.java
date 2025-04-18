package com.tkt.basejava.basejava1.basejava2.ui.language;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.base.BaseActivity;
import com.tkt.basejava.basejava1.basejava2.databinding.ActivityLanguageBinding;
import com.tkt.basejava.basejava1.basejava2.ui.home.HomeActivity;
import com.tkt.basejava.basejava1.basejava2.ui.language.adapter.LanguageAdapter;
import com.tkt.basejava.basejava1.basejava2.ui.language.model.LanguageModel;
import com.tkt.basejava.basejava1.basejava2.util.SPUtils;
import com.tkt.basejava.basejava1.basejava2.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;
    String nameLang;

    @Override
    public ActivityLanguageBinding getBinding() {
        return ActivityLanguageBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        initData();
        codeLang = Locale.getDefault().getLanguage();
        binding.ivGone.setVisibility(View.VISIBLE);
        binding.tvTitle.setText(getString(R.string.language));
        nameLang = SPUtils.getString(this, SPUtils.LANGUAGE, "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageAdapter languageAdapter = new LanguageAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
        }, this);


        languageAdapter.setCheck(SystemUtil.getPreLanguage(getBaseContext()));

        binding.rcvLang.setLayoutManager(linearLayoutManager);
        binding.rcvLang.setAdapter(languageAdapter);
    }

    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(view -> {
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SPUtils.setString(this, SPUtils.LANGUAGE, nameLang);
            startNextActivity(HomeActivity.class, null);
            finishAffinity();
        });
        binding.ivBack.setOnClickListener(v -> onBack());
    }

    @Override
    public void onBack() {
        finishThisActivity();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel(getString(R.string.china_simplified), "zh-rCN", false));
        listLanguage.add(new LanguageModel(getString(R.string.china_traditional), "zh-rTW", false));
        listLanguage.add(new LanguageModel(getString(R.string.hindi), "hi", false));
        listLanguage.add(new LanguageModel(getString(R.string.english), "en", false));
        listLanguage.add(new LanguageModel(getString(R.string.spanish), "es", false));
        listLanguage.add(new LanguageModel(getString(R.string.portuguese_brazil), "pt-rBR", false));
        listLanguage.add(new LanguageModel(getString(R.string.portuguese_portugal), "pt-rPT", false));
        listLanguage.add(new LanguageModel(getString(R.string.french), "fr", false));
        listLanguage.add(new LanguageModel(getString(R.string.bengali), "bn", false));
        listLanguage.add(new LanguageModel(getString(R.string.russian), "ru", false));
        listLanguage.add(new LanguageModel(getString(R.string.german), "de", false));
        listLanguage.add(new LanguageModel(getString(R.string.japanese), "ja", false));
        listLanguage.add(new LanguageModel(getString(R.string.turkey), "tr", false));
        listLanguage.add(new LanguageModel(getString(R.string.korean), "ko", false));
        listLanguage.add(new LanguageModel(getString(R.string.indonesia), "id", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

}
