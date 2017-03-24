package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

/**
 * Created by Madiba on 3/14/2017.
 */
@Parcel
public class VenuFile {

    String url;
    Float size;
    int Type;
    Boolean edited=false;

    public VenuFile() {
    }

    public VenuFile(String url, int type) {
        this.url = url;
        Type = type;
    }

    public VenuFile(String url, int type, Boolean edited) {
        this.url = url;
        Type = type;
        this.edited = edited;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }
}
