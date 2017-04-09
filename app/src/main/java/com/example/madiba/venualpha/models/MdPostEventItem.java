package com.example.madiba.venualpha.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdPostEventItem {

    int type = 0;
    String hashtag = null;    //Event
    String Title ;
    String Location ;
    LatLng cordinate ;
    Date Date ;
    Date Time ;
    String Desc;
    String path;
    String contact;
    Boolean isBuyAble =false;
    Boolean isVideo =false;
    String Price ;
    String category;
    String features;
    int theme;

    List<MdUserItem> invites;


    public LatLng getCordinate() {
        return cordinate;
    }

    public void setCordinate(LatLng cordinate) {
        this.cordinate = cordinate;
    }

    public MdPostEventItem() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public java.util.Date getTime() {
        return Time;
    }

    public void setTime(java.util.Date time) {
        Time = time;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getBuyAble() {
        return isBuyAble;
    }

    public void setBuyAble(Boolean buyAble) {
        isBuyAble = buyAble;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public List<MdUserItem> getInvites() {
        return invites;
    }

    public void setInvites(List<MdUserItem> invites) {
        this.invites = invites;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }
}
