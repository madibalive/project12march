package com.example.madiba.venualpha.models;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 2/22/2017.
 */

public class MConversationItem {
    public static final int TYPE_DEFAULT = 1;
    public static final int TYPE_GROUP = 2;

    private String title;
    private String name;
    private String date;
    private boolean status;
    private int Type;
    private List<String> avatarList = new ArrayList<>();
    private String avatar ;
    private ParseObject object;

    public MConversationItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public List<String> getAvatarList() {
        return avatarList;
    }

    public void setAvatarList(List<String> avatarList) {
        this.avatarList = avatarList;
    }

    public ParseObject getObject() {
        return object;
    }

    public void setObject(ParseObject object) {
        this.object = object;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
