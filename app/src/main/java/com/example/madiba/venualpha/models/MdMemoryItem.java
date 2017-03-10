package com.example.madiba.venualpha.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Madiba on 2/27/2017.
 */
@Parcel
public class MdMemoryItem {
    static final int TYPE_SHARED=0;
    static final int TYPE_SINGLE=1;

    String parseId  ;
    public String hashTag ;
    public String dateToString ;

    int  memberCount;
    int mediaCount;
    int views;

    public Date date ;

    MdUserItem userItem;
    MdEventItem eventItem;

    List<MdMediaItem> mediaItems;
    List<MdUserItem> memberList;
    ArrayList<String> memberListToString= new ArrayList<>();

    int type = 0;

    public MdMemoryItem() {
    }


    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
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

    public List<MdMediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MdMediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public List<MdUserItem> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MdUserItem> memberList) {
        this.memberList = memberList;
    }

    public ArrayList<String> getStrings() {
        return memberListToString;
    }

    public void setStrings(ArrayList<String> memberListToString) {
        this.memberListToString = memberListToString;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getMemberListToString() {
        return memberListToString;
    }

    public void setMemberListToString(ArrayList<String> memberListToString) {
        this.memberListToString = memberListToString;
    }
}
