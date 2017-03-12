package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdLiveNowItem {

    MdEventItem eventItem;
    List<MdMemoryItem> mdMemoryItems;

    public MdLiveNowItem() {
    }

    public MdEventItem getEventItem() {
        return eventItem;
    }

    public void setEventItem(MdEventItem eventItem) {
        this.eventItem = eventItem;
    }

    public List<MdMemoryItem> getMdMemoryItems() {
        return mdMemoryItems;
    }

    public void setMdMemoryItems(List<MdMemoryItem> mdMemoryItems) {
        this.mdMemoryItems = mdMemoryItems;
    }
}