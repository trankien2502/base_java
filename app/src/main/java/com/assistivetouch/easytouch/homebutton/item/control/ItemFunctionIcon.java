package com.assistivetouch.easytouch.homebutton.item.control;

public class ItemFunctionIcon {
    public static final int ACTION_SCREEN_RECORDER = 1;
    public static final int ACTION_BLUETOOTH = 2;
    public static final int ACTION_AIRPLANE = 3;
    public static final int ACTION_LOCATION = 4;
    public static final int ACTION_LOCK_ROTATION = 5;
    public static final int ACTION_FLASHLIGHT = 6;
    public static final int ACTION_VOLUME_OPTION = 7;
    public static final int ACTION_TIME_OUT = 8;
    public static final int ACTION_ALL_APP = 9;
    public static final int ACTION_HOME = 10;
    public static final int ACTION_WIFI = 11;
    public static final int ACTION_BRIGHTNESS = 12;
    public static final int ACTION_DEVICE = 13;
    public static final int ACTION_SCREEN_SHOT = 14;
    public static final int ACTION_NOTIFICATION = 15;
    public static final int ACTION_FAVOURITE = 16;
    public static final int ACTION_RECENT = 17;
    public static final int ACTION_LOCK_SCREEN = 18;
    public static final int ACTION_SETTINGS = 19;
    public static final int ACTION_BACK = 20;
    public static final int ACTION_NONE = 21;
    public static final int ACTION_VOLUME_DOWN = 22;
    public static final int ACTION_VOLUME_UP = 23;
    public static final int ACTION_OPEN_MENU = 24;
    public static final int ACTION_POWER = 25;
    public static final int ACTION_CAMERA = 26;
    private int id;
    private int actionNumber;
    private int icon;
    private int iconShow;
    private int text;
    private boolean isSelect;

    public ItemFunctionIcon(int actionNumber,int icon, int iconShow, int text) {
        this.icon = icon;
        this.text = text;
        this.iconShow = iconShow;
        this.actionNumber = actionNumber;
        this.isSelect = false;
    }

    public ItemFunctionIcon(int id,int actionNumber, int icon, int iconShow, int text) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.iconShow = iconShow;
        this.actionNumber = actionNumber;
        this.isSelect = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int actionNumber) {
        this.actionNumber = actionNumber;
    }

    public int getIconShow() {
        return iconShow;
    }

    public void setIconShow(int iconShow) {
        this.iconShow = iconShow;
    }
}
