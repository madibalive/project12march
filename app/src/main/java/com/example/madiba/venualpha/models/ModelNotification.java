package com.example.madiba.venualpha.models;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 12/30/2016.
 */

public class ModelNotification {
    public static final int TYPE_DEFAULT=1;
    public static final int TYPE_EXTENDED=2;
    public static final int TYPE_SUGGESTTED=3;
    public int type;

    private List<ParseObject>  data;


    public ModelNotification() {
    }

    public ModelNotification(int type, List<ParseObject> data) {
        this.type = type;
        this.data = data;
    }

    public ModelNotification(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ParseObject> getData() {
        return data;
    }

    public void setData(List<ParseObject> data) {
        this.data = data;
    }
}
