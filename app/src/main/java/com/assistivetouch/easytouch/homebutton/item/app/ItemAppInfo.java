package com.assistivetouch.easytouch.homebutton.item.app;

import android.graphics.drawable.Drawable;

public class ItemAppInfo {
    int id;
    String name;
    String packageName;
    Drawable icon;

    public ItemAppInfo(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    public ItemAppInfo(int id, String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
        this.id = id;
    }

    public ItemAppInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ItemAppInfo(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
