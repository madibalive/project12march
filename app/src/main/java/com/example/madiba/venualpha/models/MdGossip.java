package com.example.madiba.venualpha.models;


import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Madiba on 11/3/2016.
 */
@Parcel
public class MdGossip {

    String parseId;
    String title;
    String color;
    String urlSmall;
    String urlLarge;
    int views;
    Boolean status;
    public Date date = null;
    public String dateToString;
    MdUserItem userItem;
    MdUserItem sharedBy;
    Boolean isShared=false;
    Boolean extraAvailable=false;
    String extraInfo;



    public MdGossip() {
    }

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrlLarge() {
        return urlLarge;
    }

    public void setUrlLarge(String urlLarge) {
        this.urlLarge = urlLarge;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public MdUserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(MdUserItem userItem) {
        this.userItem = userItem;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public MdUserItem getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(MdUserItem sharedBy) {
        this.sharedBy = sharedBy;
    }

    public Boolean getShared() {
        return isShared;
    }

    public void setShared(Boolean shared) {
        isShared = shared;
    }

    public Boolean getExtraAvailable() {
        return extraAvailable;
    }

    public void setExtraAvailable(Boolean extraAvailable) {
        this.extraAvailable = extraAvailable;
    }
}