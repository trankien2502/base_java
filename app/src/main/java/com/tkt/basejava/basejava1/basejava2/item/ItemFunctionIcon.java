package com.tkt.basejava.basejava1.basejava2.item;

public class ItemFunctionIcon {
    private int id;
    private int icon;
    private int text;
    private boolean isSelect;

    public ItemFunctionIcon(int icon, int text) {
        this.icon = icon;
        this.text = text;
        this.isSelect = false;
    }

    public ItemFunctionIcon(int id, int icon, int text) {
        this.id = id;
        this.text = text;
        this.icon = icon;
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
}
