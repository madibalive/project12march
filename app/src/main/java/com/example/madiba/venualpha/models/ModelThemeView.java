package com.example.madiba.venualpha.models;

public class ModelThemeView {

    private  int mResId;
    private static final int WHITE=1;
    private static final int DARK=2;
    private static final int TRANSPARENT=3;


    private int type;
    private Boolean selected=false;


    public ModelThemeView( final int resId,int type) {
        this.type=type;
        mResId = resId;

    }

    public int getDrawableId() {
        return mResId;
    }

    public int getType() {
        return type;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

