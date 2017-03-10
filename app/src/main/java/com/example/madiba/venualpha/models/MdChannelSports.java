package com.example.madiba.venualpha.models;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 12/30/2016.
 */

public class MdChannelSports {


    private List<ParseObject>  data;


    public MdChannelSports() {
    }

    public MdChannelSports(List<ParseObject> data) {
        this.data = data;
    }

    public List<ParseObject> getData() {
        return data;
    }

    public void setData(List<ParseObject> data) {
        this.data = data;
    }
}
