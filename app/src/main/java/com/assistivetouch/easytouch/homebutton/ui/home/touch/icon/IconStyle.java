package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import java.io.Serializable;

public class IconStyle implements Serializable {
    private int source;
    private boolean isSelect;

    public IconStyle() {
    }

    public IconStyle(int source) {
        this.source = source;
        this.isSelect = false;
    }

    public IconStyle(int source, boolean isSelect) {
        this.source = source;
        this.isSelect = isSelect;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
