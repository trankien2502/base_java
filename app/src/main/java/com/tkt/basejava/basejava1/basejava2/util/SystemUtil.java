package com.tkt.basejava.basejava1.basejava2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class SystemUtil {
    private static Locale myLocale;

    public static void saveLocale(Context context, String lang) {
        setPreLanguage(context, lang);
    }

    public static void setLocale(Context context) {
        String language = getPreLanguage(context);
        if (language.equals("")) {
            Configuration config = new Configuration();
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        } else {
            changeLang(context, language);
        }
    }

    public static void changeLang(String lang, Context context) {
        if (lang.equalsIgnoreCase("")) return;
        myLocale = new Locale(lang);
        saveLocale(context, lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void changeLang(Context context, String lang) {
        if (lang.equalsIgnoreCase("")) return;

        String[] localeParts = lang.split("-");
        Configuration config = new Configuration();

        if (localeParts.length > 1) {
            config.locale = new Locale(localeParts[0], localeParts[1]);
            myLocale = new Locale(localeParts[0], localeParts[1]);
        } else {
            config.locale = new Locale(lang);
            myLocale = new Locale(lang);
        }
        saveLocale(context, lang);
        Locale.setDefault(myLocale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getPreLanguage(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        return preferences.getString("KEY_LANGUAGE", "en");
    }

    public static void setPreLanguage(Context context, String language) {
        if (language == null || language.equals("")) {
        } else {
            SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("KEY_LANGUAGE", language);
            editor.apply();
        }
    }
}
