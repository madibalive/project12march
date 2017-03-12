package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdEventRowItem {

    List<MdEventItem> mdMemoryItems;

    public MdEventRowItem() {
    }

    public List<MdEventItem> getMdMemoryItems() {
        return mdMemoryItems;
    }

    public void setMdMemoryItems(List<MdEventItem> mdMemoryItems) {
        this.mdMemoryItems = mdMemoryItems;
    }
}