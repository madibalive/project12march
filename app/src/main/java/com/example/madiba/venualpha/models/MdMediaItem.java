package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdMediaItem {

    static final int TYPE_IMAGE=0;
    static final int TYPE_VIDEO=1;

    String parseId  ;
    String urlSmall;
    String urlMedium;
    String urlLarge;
    public String hashtag ;

    int  commentCount;
    int rxnCount;
    int views;
    int type = 0;

    public Date date = null;
    public String dateToString ;

    Boolean status;

    MdUserItem userItem;
    MdEventItem eventItem;


    public MdMediaItem() {
    }


    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
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

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getRxnCount() {
        return rxnCount;
    }

    public void setRxnCount(int rxnCount) {
        this.rxnCount = rxnCount;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MdUserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(MdUserItem userItem) {
        this.userItem = userItem;
    }

    public MdEventItem getEventItem() {
        return eventItem;
    }

    public void setEventItem(MdEventItem eventItem) {
        this.eventItem = eventItem;
    }
}
