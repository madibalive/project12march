package com.example.madiba.venualpha.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdPostMemoryItem {


    String caption = null;    //Event
    MdEventItem mdEventItem;
    String hashTag;

    List<MdUserItem> invites = new ArrayList<>();
    List<VenuFile> urls= new ArrayList<>();
    VenuFile venuFile;

    Boolean isLinked;
    Boolean isNew;
    Boolean isVideo;


    public MdPostMemoryItem() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public MdEventItem getMdEventItem() {
        return mdEventItem;
    }

    public void setMdEventItem(MdEventItem mdEventItem) {
        this.mdEventItem = mdEventItem;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public List<MdUserItem> getInvites() {
        return invites;
    }

    public void setInvites(List<MdUserItem> invites) {
        this.invites = invites;
    }

    public List<VenuFile> getUrls() {
        return urls;
    }

    public void setUrls(List<VenuFile> urls) {
        this.urls = urls;
    }

    public VenuFile getVenuFile() {
        return venuFile;
    }

    public void setVenuFile(VenuFile venuFile) {
        this.venuFile = venuFile;
    }

    public Boolean getLinked() {
        return isLinked;
    }

    public void setLinked(Boolean linked) {
        isLinked = linked;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }
}
