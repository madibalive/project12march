package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdEventItem {

    //Parse
    String parseId=null;
    MdUserItem userItem;

    int comment = 0;
    int trackers = 0; // reactions

    Date date = null;
    String dateToString = null;

    String hashtag = null;
    String urlSmall = null;
    String urlMedium = null;
    String urlLarge = null;

    Boolean status = false;

    int type = 0;


    //Event
    String evTitle ;
    String evLocation ;
    Date evDate ;
    String evDateToString ;
    Date evTime ;
    String evTimeToString ;
    String evDesc;
    String evStatus;
    Boolean isBuyAble =false;
    Boolean active =false;
    int evPrice = 0;

    MdUserItem sharedBy;
    Boolean isShared=false;
    Boolean extraAvailable=false;
    String extraInfo;


    public MdEventItem() {
    }


    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public MdUserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(MdUserItem userItem) {
        this.userItem = userItem;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getTrackers() {
        return trackers;
    }

    public void setTrackers(int trackers) {
        this.trackers = trackers;
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

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrlMedium() {
        return urlMedium;
    }

    public void setUrlMedium(String urlMedium) {
        this.urlMedium = urlMedium;
    }

    public String getUrlLarge() {
        return urlLarge;
    }

    public void setUrlLarge(String urlLarge) {
        this.urlLarge = urlLarge;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEvTitle() {
        return evTitle;
    }

    public void setEvTitle(String evTitle) {
        this.evTitle = evTitle;
    }

    public String getEvLocation() {
        return evLocation;
    }

    public void setEvLocation(String evLocation) {
        this.evLocation = evLocation;
    }

    public Date getEvDate() {
        return evDate;
    }

    public void setEvDate(Date evDate) {
        this.evDate = evDate;
    }

    public String getEvDateToString() {
        return evDateToString;
    }

    public void setEvDateToString(String evDateToString) {
        this.evDateToString = evDateToString;
    }

    public Date getEvTime() {
        return evTime;
    }

    public void setEvTime(Date evTime) {
        this.evTime = evTime;
    }

    public String getEvTimeToString() {
        return evTimeToString;
    }

    public void setEvTimeToString(String evTimeToString) {
        this.evTimeToString = evTimeToString;
    }

    public String getEvDesc() {
        return evDesc;
    }

    public void setEvDesc(String evDesc) {
        this.evDesc = evDesc;
    }

    public String getEvStatus() {
        return evStatus;
    }

    public void setEvStatus(String evStatus) {
        this.evStatus = evStatus;
    }

    public Boolean getBuyAble() {
        return isBuyAble;
    }

    public void setBuyAble(Boolean buyAble) {
        isBuyAble = buyAble;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getEvPrice() {
        return evPrice;
    }

    public void setEvPrice(int evPrice) {
        this.evPrice = evPrice;
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
