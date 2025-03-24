package com.livescore.soccerscore.matchlive.ui.language;

import static android.view.View.GONE;

import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.ui.intro.IntroActivity;
import com.livescore.soccerscore.matchlive.ui.language.adapter.LanguageStartAdapter;
import com.livescore.soccerscore.matchlive.ui.language.model.LanguageModel;
import com.livescore.soccerscore.matchlive.util.EventTracking;
import com.livescore.soccerscore.matchlive.util.SPUtils;
import com.livescore.soccerscore.matchlive.util.SystemUtil;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.databinding.ActivityLanguageStartBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageStartActivity extends BaseActivity<ActivityLanguageStartBinding> {

    List<LanguageModel> listLanguage;
    String codeLang;
    String nameLang;

    @Override
    public ActivityLanguageStartBinding getBinding() {
        return ActivityLanguageStartBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "language_fo_open");
        initData();

//        binding.view.setOnClickListener(v -> {
//            binding.view.setVisibility(GONE);
//            binding.pointer.setVisibility(GONE);
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
            binding.pointer.setVisibility(GONE);
        }, this);
        binding.rcvLangStart.setLayoutManager(linearLayoutManager);
        binding.rcvLangStart.setAdapter(languageStartAdapter);
    }

    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(view -> {
            EventTracking.logEvent(this, "language_fo_save_click");
            if (codeLang == null || codeLang.isEmpty()) {
                Toast.makeText(this, R.string.please_select_a_language, Toast.LENGTH_SHORT).show();
                return;
            }
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SPUtils.setString(this, SPUtils.LANGUAGE, nameLang);
            startNextActivity(IntroActivity.class, null);
            finishAffinity();
        });
    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel(getString(R.string.china_simplified), "zh-CN", false));
        listLanguage.add(new LanguageModel(getString(R.string.china_traditional), "zh-TW", false));
        listLanguage.add(new LanguageModel(getString(R.string.hindi), "hi", false));
        listLanguage.add(new LanguageModel(getString(R.string.english), "en", false));
        listLanguage.add(new LanguageModel(getString(R.string.spanish), "es", false));
        listLanguage.add(new LanguageModel(getString(R.string.portuguese_brazil), "pt-BR", false));
        listLanguage.add(new LanguageModel(getString(R.string.portuguese_portugal), "pt-PT", false));
        listLanguage.add(new LanguageModel(getString(R.string.french), "fr", false));
        listLanguage.add(new LanguageModel(getString(R.string.bengali), "bn", false));
        listLanguage.add(new LanguageModel(getString(R.string.russian), "ru", false));
        listLanguage.add(new LanguageModel(getString(R.string.german), "de", false));
        listLanguage.add(new LanguageModel(getString(R.string.japanese), "ja", false));
        listLanguage.add(new LanguageModel(getString(R.string.turkey), "tr", false));
        listLanguage.add(new LanguageModel(getString(R.string.korean), "ko", false));
        listLanguage.add(new LanguageModel(getString(R.string.indonesia), "id", false));

//        for (int i = 0; i < listLanguage.size(); i++) {
//            if (listLanguage.get(i).getCode().equals(lang)) {
//                listLanguage.add(0, listLanguage.get(i));
//                listLanguage.remove(i + 1);
//            }
//        }
    }

}
