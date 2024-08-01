package com.tkt.spin_wheel.ui.language;

import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.tkt.spin_wheel.base.BaseActivity;
import com.tkt.spin_wheel.ui.intro.IntroActivity;
import com.tkt.spin_wheel.ui.language.adapter.LanguageStartAdapter;
import com.tkt.spin_wheel.ui.language.model.LanguageModel;
import com.tkt.spin_wheel.util.EventTracking;
import com.tkt.spin_wheel.util.SPUtils;
import com.tkt.spin_wheel.util.SystemUtil;
import com.tkt.spin_wheel.R;
import com.tkt.spin_wheel.databinding.ActivityLanguageStartBinding;

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
        EventTracking.logEvent(this,"language_fo_open");
        initData();
        binding.tvTitle.setText(getString(R.string.language));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageStartAdapter languageStartAdapter = new LanguageStartAdapter(listLanguage, languageModel -> {
            codeLang = languageModel.getCode();
            nameLang = languageModel.getName();
            }, this);
        binding.rcvLangStart.setLayoutManager(linearLayoutManager);
        binding.rcvLangStart.setAdapter(languageStartAdapter);
    }

    @Override
    public void bindView() {
        binding.ivGone.setOnClickListener(view -> {
            EventTracking.logEvent(this,"language_fo_save_click");
            if (codeLang==null || codeLang.isEmpty()){
                Toast.makeText(this, R.string.please_select_a_language, Toast.LENGTH_SHORT).show();
                return;
            }
            SystemUtil.saveLocale(getBaseContext(), codeLang);
            SPUtils.setString(this,SPUtils.LANGUAGE,nameLang);
            startNextActivity(IntroActivity.class, null);
            finishAffinity();
        });
    }

    private void initData() {
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en", false));
        listLanguage.add(new LanguageModel("China", "zh", false));
        listLanguage.add(new LanguageModel("French", "fr", false));
        listLanguage.add(new LanguageModel("German", "de", false));
        listLanguage.add(new LanguageModel("Hindi", "hi", false));
        listLanguage.add(new LanguageModel("Indonesia", "in", false));
        listLanguage.add(new LanguageModel("Portuguese", "pt", false));
        listLanguage.add(new LanguageModel("Spanish", "es", false));

        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
