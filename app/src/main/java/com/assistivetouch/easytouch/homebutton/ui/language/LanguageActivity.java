package com.assistivetouch.easytouch.homebutton.ui.language;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseActivity;
import com.assistivetouch.easytouch.homebutton.databinding.ActivityLanguageBinding;
import com.assistivetouch.easytouch.homebutton.ui.home.HomeActivity;
import com.assistivetouch.easytouch.homebutton.ui.language.adapter.LanguageAdapter;
import com.assistivetouch.easytouch.homebutton.ui.language.model.LanguageModel;
import com.assistivetouch.easytouch.homebutton.util.SPUtils;
import com.assistivetouch.easytouch.homebutton.util.SystemUtil;

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

}
